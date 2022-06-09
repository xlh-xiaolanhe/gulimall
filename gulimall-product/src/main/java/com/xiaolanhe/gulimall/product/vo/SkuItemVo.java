package com.xiaolanhe.gulimall.product.vo;

import com.xiaolanhe.gulimall.product.entity.SkuImagesEntity;
import com.xiaolanhe.gulimall.product.entity.SkuInfoEntity;
import com.xiaolanhe.gulimall.product.entity.SpuInfoDescEntity;
import lombok.Data;

import java.util.List;

/**
 *@author: xiaolanhe
 *@createDate: 2022/6/9 21:48
 */

@Data
public class SkuItemVo {

    //1、sku基本信息的获取  pms_sku_info
    private SkuInfoEntity info;

    private boolean hasStock = true;

    //2、sku的图片信息    pms_sku_images
    private List<SkuImagesEntity> images;

    //3、获取spu的销售属性组合
    private List<SkuItemSaleAttrVo> saleAttr;

    //4、获取spu的介绍
    private SpuInfoDescEntity desc;

    //5、获取spu的规格参数信息
    private List<SpuItemAttrGroupVo> groupAttrs;


}
