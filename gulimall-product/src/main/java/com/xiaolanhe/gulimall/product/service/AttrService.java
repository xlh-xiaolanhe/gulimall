package com.xiaolanhe.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaolanhe.common.utils.PageUtils;
import com.xiaolanhe.gulimall.product.entity.AttrEntity;
import com.xiaolanhe.gulimall.product.vo.AttrGroupRelationVo;
import com.xiaolanhe.gulimall.product.vo.AttrRespVo;
import com.xiaolanhe.gulimall.product.vo.AttrVo;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author xiaolanhe@163.com
 * @email sunlightcs@gmail.com
 * @date 2022-04-26 22:46:33
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveAttr(AttrVo attr);

    PageUtils queryBaseAttrPage(Map<String, Object> params, Long catelogId, String type);

    AttrRespVo getAttrInfo(Long attrId);

    void updateAttr(AttrVo attr);

    List<AttrEntity> getRelationAttr(Long attrgroupId);

    void deleteRelation(AttrGroupRelationVo[] vos);

    PageUtils getNoRelationAttr(Map<String, Object> params, Long attrgroupId);

    // 在指定的所有属性集合里面，挑选出可检索的属性
    List<Long> selectSearchAttIds(List<Long> attrIds);
}

