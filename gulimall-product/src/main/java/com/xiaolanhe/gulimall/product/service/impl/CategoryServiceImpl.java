package com.xiaolanhe.gulimall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.xiaolanhe.gulimall.product.service.CategoryBrandRelationService;
import com.xiaolanhe.gulimall.product.vo.CateLog2Vo;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaolanhe.common.utils.PageUtils;
import com.xiaolanhe.common.utils.Query;

import com.xiaolanhe.gulimall.product.dao.CategoryDao;
import com.xiaolanhe.gulimall.product.entity.CategoryEntity;
import com.xiaolanhe.gulimall.product.service.CategoryService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Autowired
    CategoryBrandRelationService categoryBrandRelationService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private RedissonClient redissonClient;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {
        // 查出所有分类
        List<CategoryEntity> entities = baseMapper.selectList(null);

        // 组装成父子的树形结构
            // 找到所有的一级分类(一级分类的父id为0)
        List<CategoryEntity> level1Menus = entities.stream().filter(categoryEntity -> {
            return categoryEntity.getParentCid() == 0;
        }).map((menu) -> {
            menu.setChildren(getChildrens(menu, entities));
            return menu;
        }).sorted((menu1, menu2) -> {
            return (menu1.getSort() == null ? 0 : menu1.getSort()) - (menu2.getSort() == null ? 0 : menu2.getSort());
        }).collect(Collectors.toList());

        return level1Menus;
    }

    // 递归查找所有菜单的子菜单
    private List<CategoryEntity> getChildrens(CategoryEntity root, List<CategoryEntity> all){

        List<CategoryEntity> children = all.stream().filter(categoryEntity -> {
            // 1. 找到子菜单
            return categoryEntity.getParentCid() == root.getCatId();
        }).map(categoryEntity -> {
            // 2. 递归找到子菜单的子菜单
            categoryEntity.setChildren(getChildrens(categoryEntity, all));
            return categoryEntity;
        }).sorted((menu1, menu2) -> {
            // 2. 子菜单的排序
            return (menu1.getSort() == null ? 0 : menu1.getSort()) - (menu2.getSort() == null ? 0 : menu2.getSort());
        }).collect(Collectors.toList());

        return children;
    }

    @Override
    public void removeMenueByIds(List<Long> asList) {
        // TODO 检查当前删除的菜单，是否被别的地方引用

        //逻辑删除， 而非物理删除
        baseMapper.deleteBatchIds(asList);
    }

    @Override
    public Long[] findCatelogPath(Long catelogId) {
        List<Long> paths = new ArrayList<>();
        List<Long> parentPath = findParentPath(catelogId, paths);

        Collections.reverse(parentPath);
        return parentPath.toArray(new Long[parentPath.size()]);
    }

    private List<Long> findParentPath(Long catelogId, List<Long> paths){
        paths.add(catelogId); // 收集当前节点id
        CategoryEntity byId = this.getById(catelogId);
        if(byId.getParentCid() != 0){
            findParentPath(byId.getParentCid(), paths);
        }
        return paths;
    }

    /**
     * desciption: 级联更新所有数据
      * @param category
    */
    @Transactional
    @Override
    public void updateCascade(CategoryEntity category) {
        this.updateById(category);
        categoryBrandRelationService.updateCategory(category.getCatId(), category.getName());

        // 同时修改缓存中的数据
    }

    //1. 每一个需要缓存的数据由我们自己来指定放到哪个名字的缓存(缓存的分区)
    //2. 代表当前方法的结果需要缓存，如果缓存中有，直接返回结果。如果缓存中没有，才调用方法，最后把结果保存到缓存中
    //3. 默认行为：
            // 如果缓存命中，方法不调用
            // key默认自动生成，缓存的名字: SimpleKey[] (自主生成的key值)
            // 缓存的value的值，默认使用jdk序列化机制，将序列化后的数据存到redis
            // TTL默认设置为 -1

    // 4. 自定义：
        // a、指定生成的缓存使用的key: key属性指定，接受一个SpEl表达式
        // b、指定数据的存活时间: 配置文件中配置
        // c、将数据保存为json格式
                // 自定义RedisCacheConfiguration即可

    // 5. Spring-Cache的不足:
        // 读模式：
            // 缓存穿透:  cache-null-values=true
            // 缓存雪崩: redis.time-to-live
            // 缓存击穿: 加锁，默认是无锁的， sync = true;
        /**

         原理： CacheManager(RedisCacheManager) -> Cache(RedisCache) -> Cache负责缓存的读写
         总结：
                常规数据:(读多写少，即时性，一致性要求不高的数据)，完全可以使用Spring-Cache
                    写模式（只要有缓存的过期时间就可以了）

                特殊数据： 需要特殊设计
        */


    @Cacheable(value = {"category"}, key = "'level1Category'")
    @Override
    public List<CategoryEntity> getLevelOneCategorys() {
        List<CategoryEntity> categoryEntities = baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", 0));
        return categoryEntities;
    }

    //TODO 产生堆外内存溢出OutOfDirectMemoryError:
    //1)、springboot2.0以后默认使用lettuce操作redis的客户端，它使用通信
    //2)、lettuce的bug导致netty堆外内存溢出   可设置：-Dio.netty.maxDirectMemory
    //解决方案：不能直接使用-Dio.netty.maxDirectMemory去调大堆外内存
    //1)、升级lettuce客户端。      2）、切换使用jedis
    @Override
    public Map<String, List<CateLog2Vo>> getCatalogJson(){
        // 给缓存中放json字符串，拿出的json字符串，还能逆转为能用的对象类型(序列化与反序列化)

        // 加入缓存逻辑 缓存中存的数据是json字符串(json 跨语言 跨平台兼容)
        String catalogJSON = redisTemplate.opsForValue().get("catalogJSON");
        if(StringUtils.isEmpty(catalogJSON)){
            Map<String, List<CateLog2Vo>> catalogJsonFromDB = getCatalogJsonFromDB();

            return catalogJsonFromDB;
        }

        // 转为我们指定的对象
        Map<String, List<CateLog2Vo>> result = JSON.parseObject(catalogJSON,
                new TypeReference<Map<String, List<CateLog2Vo>>>(){});

        return result;
    }

    // 从数据库查询并封装分类数据
    public Map<String, List<CateLog2Vo>> getCatalogJsonFromDB() {

        // 只要是同一把锁，就能锁住需要这个锁的所有线程
        // synchronized(this) SpringBoot所有的组件在容器中都是单例的

        synchronized (this){
            //得到锁以后，我们应该再去缓存中确定一次，如果没有才需要继续查询
            String catalogJson = redisTemplate.opsForValue().get("catalogJson");
            if (!StringUtils.isEmpty(catalogJson)) {
                //缓存不为空直接返回
                Map<String, List<CateLog2Vo>> result = JSON.parseObject(catalogJson,
                        new TypeReference<Map<String, List<CateLog2Vo>>>(){});

                return result;
            }

            System.out.println("查询了数据库");

            // 将数据库得多次查询变为 1 次
            List<CategoryEntity> selectList = baseMapper.selectList(null);

            // 1.查出所有 1 级分类
            List<CategoryEntity> levelOneCategorys = getParent_id(selectList, 0L);

            // 2. 封装数据
            Map<String, List<CateLog2Vo>> parent_cid = levelOneCategorys.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
                // 根据每一个一级分类，查询这个一级分类的所有二级分类
                List<CategoryEntity> categoryEntities = getParent_id(selectList, v.getCatId());

                // 封装成指定要的List
                List<CateLog2Vo> cateLog2Vos = null;
                if (categoryEntities != null) {
                    cateLog2Vos = categoryEntities.stream().map(l2 -> {
                        CateLog2Vo cateLog2Vo = new CateLog2Vo(v.getCatId().toString(), null,
                                l2.getCatId().toString(), l2.getName());

                        // 找当前二级分类的三级分类封装成 vo
                        List<CategoryEntity> level3Catelog = getParent_id(selectList, l2.getCatId());
                        if(level3Catelog != null){

                            List<CateLog2Vo.Catelog3Vo> collect = level3Catelog.stream().map(l3 -> {
                                // 封装成指定格式的数据
                                CateLog2Vo.Catelog3Vo catelog3Vo = new CateLog2Vo.Catelog3Vo(l2.getCatId().toString(), l3.getCatId().toString(), l3.getName());
                                return catelog3Vo;
                            }).collect(Collectors.toList());

                            cateLog2Vo.setCatalog3List(collect);
                        }
                        return cateLog2Vo;
                    }).collect(Collectors.toList());
                }

                return cateLog2Vos;
            }));

            // 查询到的数据对象转为json放在缓存中
            String s = JSON.toJSONString(parent_cid);
            redisTemplate.opsForValue().set("catalogJSON", s);

            return parent_cid;
        }
    }

    // 使用redisson加锁
    /**
     * 缓存里的数据如何和数据库的数据保持一致？？
     * 缓存数据一致性
     * 1)、双写模式
     * 2)、失效模式
     */
    public Map<String, List<CateLog2Vo>> getCatalogJsonFromDbWithRedissonLock()
    {
        //1、占分布式锁。去redis占坑 (锁的名字一样，就是同一把锁)
        //（锁的粒度，:具体缓存的是某个数据，11号商品） product-11-lock
        RLock lock = redissonClient.getLock("catalogJson-lock");
        lock.lock();

        System.out.println("获取分布式锁成功...");
        Map<String, List<CateLog2Vo>> dataFromDb = null;
        try {
            //加锁成功...执行业务
            dataFromDb = getCatalogJsonFromDB();
        } finally {
           lock.unlock();
        }
        return dataFromDb;
    }


    /**
     * 从数据库查询并封装数据::分布式锁
     * @return
     */
    public Map<String, List<CateLog2Vo>> getCatalogJsonFromDbWithRedisLock() {

        //1、占分布式锁。去redis占坑      设置过期时间必须和加锁是同步的，保证原子性（避免死锁）
        String uuid = UUID.randomUUID().toString();
        Boolean lock = redisTemplate.opsForValue().setIfAbsent("lock", uuid,300, TimeUnit.SECONDS);
        if (lock) {
            System.out.println("获取分布式锁成功...");
            Map<String, List<CateLog2Vo>> dataFromDb = null;
            try {
                //加锁成功...执行业务
                dataFromDb = getCatalogJsonFromDB();
            } finally {
                String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

                //删除锁
                redisTemplate.execute(new DefaultRedisScript<Long>(script, Long.class), Arrays.asList("lock"), uuid);

            }
            //先去redis查询下保证当前的锁是自己的
            //获取值对比，对比成功删除=原子性 lua脚本解锁
            // String lockValue = redisTemplate.opsForValue().get("lock");
            // if (uuid.equals(lockValue)) {
            //     //删除我自己的锁
            //     redisTemplate.delete("lock");
            // }

            return dataFromDb;
        } else {
            System.out.println("获取分布式锁失败...等待重试...");
            //加锁失败...重试机制
            //休眠一百毫秒
            try
            { TimeUnit.MILLISECONDS.sleep(100); }
            catch (InterruptedException e)
            { e.printStackTrace(); }
            return getCatalogJsonFromDbWithRedisLock();     //自旋的方式
        }
    }

    private List<CategoryEntity> getParent_id(List<CategoryEntity> selectList, Long parent_id) {
        List<CategoryEntity> collect = selectList.stream().filter(item -> item.getParentCid() == parent_id)
                .collect(Collectors.toList());
        //return baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_id", l2.getCatId()));
        return collect;
    }
}