package com.xiaolanhe.gulimall.ware.dao;

import com.xiaolanhe.gulimall.ware.entity.WareSkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 商品库存
 * 
 * @author xiaolanhe@163.com
 * @email sunlightcs@gmail.com
 * @date 2022-05-19 22:13:51
 */
@Mapper
public interface WareSkuDao extends BaseMapper<WareSkuEntity> {
    void addStock(@Param("skuId")Long skuId, @Param("wareId")Long wareId, @Param("skuNum")Integer skuNum);

    Long getSkuStock(Long skuId);
}
