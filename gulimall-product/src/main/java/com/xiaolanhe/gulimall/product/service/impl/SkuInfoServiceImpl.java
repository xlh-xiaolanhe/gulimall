package com.xiaolanhe.gulimall.product.service.impl;

import com.xiaolanhe.gulimall.product.entity.SkuImagesEntity;
import com.xiaolanhe.gulimall.product.entity.SpuInfoDescEntity;
import com.xiaolanhe.gulimall.product.entity.SpuInfoEntity;
import com.xiaolanhe.gulimall.product.service.SkuImagesService;
import com.xiaolanhe.gulimall.product.service.SpuInfoDescService;
import com.xiaolanhe.gulimall.product.vo.SkuItemVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaolanhe.common.utils.PageUtils;
import com.xiaolanhe.common.utils.Query;

import com.xiaolanhe.gulimall.product.dao.SkuInfoDao;
import com.xiaolanhe.gulimall.product.entity.SkuInfoEntity;
import com.xiaolanhe.gulimall.product.service.SkuInfoService;
import org.springframework.util.StringUtils;


@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {

    @Autowired
    SkuImagesService skuImagesService;

    @Autowired
    SpuInfoDescService spuInfoDescService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                new QueryWrapper<SkuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveSkuInfo(SkuInfoEntity skuInfoEntity) {
        this.baseMapper.insert(skuInfoEntity);
    }

    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {

       /*       page: 1,//当前页码
                limit: 10,//每页记录数
                sidx: 'id',//排序字段
                order: 'asc/desc',//排序方式

                key: '华为',//检索关键字
                catelogId: 0,
                brandId: 0,
                min: 0,
                max: 0*/

        QueryWrapper<SkuInfoEntity> queryWrapper = new QueryWrapper<>();
        String key = (String) params.get("key");
        if(!StringUtils.isEmpty(key))
        {
            queryWrapper.and((wrapper) -> {
                wrapper.eq("sku_id", key).or().like("sku_name", key);
            });
        }

        String catelogId = (String) params.get("catelogId");
        if(!StringUtils.isEmpty(catelogId) && !"0".equalsIgnoreCase(catelogId))
        {
            queryWrapper.eq("catelog_Id", catelogId);
        }

        String brandId = (String) params.get("brandId");
        if(!StringUtils.isEmpty(brandId) && !"0".equalsIgnoreCase(brandId))
        {
            queryWrapper.eq("brand_Id", brandId);
        }

        String min = (String) params.get("min");
        if(!StringUtils.isEmpty(min))
        {
            queryWrapper.ge("price", min);
        }

        String max = (String) params.get("max");
        if(!StringUtils.isEmpty(max))
        {
            try{
                BigDecimal maxValue = new BigDecimal(max);
                if(maxValue.compareTo(new BigDecimal("0")) == 1){
                    queryWrapper.le("price", max);
                }
            }catch (Exception e){

            }
        }

        IPage<SkuInfoEntity> page = this.page(new Query<SkuInfoEntity>().getPage(params), queryWrapper);

        return new PageUtils(page);

    }

    @Override
    public List<SkuInfoEntity> getSkusBySpuId(Long spuId) {
        List<SkuInfoEntity> list = this.list(new QueryWrapper<SkuInfoEntity>().eq("spu_id",spuId));
        return list;
    }

    @Override
    public SkuItemVo item(Long skuId) {

        SkuItemVo skuItemVo = new SkuItemVo();

        // sku 基本信息获取 pms_sku_info
        SkuInfoEntity info = getById(skuId);
        skuItemVo.setInfo(info);

        // sku的图片信息 pms_sku_images
        List<SkuImagesEntity> images = skuImagesService.getImagesBySkuId(skuId);
        skuItemVo.setImages(images);
        // 获取spu的销售属性组合

        //  获取spu的介绍
        Long spuId = info.getSpuId();
        SpuInfoDescEntity desp = spuInfoDescService.getById(spuId);
        skuItemVo.setDesc(desp);

        // 获取spu的规格信息

        return null;
    }
}