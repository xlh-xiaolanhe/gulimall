package com.xiaolanhe.gulimall.product.service.impl;

import com.xiaolanhe.gulimall.product.entity.AttrEntity;
import com.xiaolanhe.gulimall.product.service.AttrService;
import com.xiaolanhe.gulimall.product.vo.AttrGroupWithAttrsVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaolanhe.common.utils.PageUtils;
import com.xiaolanhe.common.utils.Query;

import com.xiaolanhe.gulimall.product.dao.AttrGroupDao;
import com.xiaolanhe.gulimall.product.entity.AttrGroupEntity;
import com.xiaolanhe.gulimall.product.service.AttrGroupService;
import org.springframework.util.StringUtils;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Autowired
    AttrService attrService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Long catelogId) {

        String key = (String)params.get("key"); // 前端传入的检索关键字
        // select * from pms_attr_group where catelog_id=? and (attr_group_id = key or attr_group_name like %key%)
        QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<AttrGroupEntity>();
        if(!StringUtils.isEmpty(key)){
            wrapper.and((obj) -> {
                obj.eq("attr_group_id", key).or().like("attr_group_name", key);
            });
        }

        IPage<AttrGroupEntity> page;
        if(catelogId == 0){
            page = this.page(new Query<AttrGroupEntity>().getPage(params),
                    wrapper);
        }else{
            wrapper.eq("catelog_id", catelogId);

            page = this.page(new Query<AttrGroupEntity>().getPage(params),
                    wrapper);
        }
        return new PageUtils(page);
    }

    /**
     * desciption:  根据分类id查出所有的分组以及这些分组里面的属性
      * @param catelogId
     * @return java.util.List<com.xiaolanhe.gulimall.product.vo.AttrGroupWithAttrsVo>
    */
    @Override
    public List<AttrGroupWithAttrsVo> getAttrGroupWithAttrsByCatelogId(Long catelogId) {
        // 1、 查询分组信息
        List<AttrGroupEntity> attrGroupEntities = this.list(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catelogId));

        // 2、查询每个分组的所有属性
        List<AttrGroupWithAttrsVo> collect = attrGroupEntities.stream().map(item -> {
            AttrGroupWithAttrsVo attrsVo = new AttrGroupWithAttrsVo();
            BeanUtils.copyProperties(item, attrsVo);
            List<AttrEntity> attrs = attrService.getRelationAttr(attrsVo.getAttrGroupId());
            attrsVo.setAttrs(attrs);
            return attrsVo;
        }).collect(Collectors.toList());

        return collect;
    }
}