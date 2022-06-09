package com.xiaolanhe.gulimall.product.vo;

import lombok.Data;

import java.util.List;

/**
 *@author: xiaolanhe
 *@createDate: 2022/6/9 22:02
 */

@Data
public class SkuItemSaleAttrVo {
    private Long attrId;

    private String attrName;

    private List<AttrValueWithSkuIdVo> attrValues;

}
