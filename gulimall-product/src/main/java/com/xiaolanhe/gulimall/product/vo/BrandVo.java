package com.xiaolanhe.gulimall.product.vo;

import lombok.Data;

/**
 *@author: xiaolanhe
 *@createDate: 2022/5/16 18:07
 */

@Data
public class BrandVo {

    /*  返回数据格式
    "brandId": 0,
	    "brandName": "string",*/

    private String brandName;

    private Long brandId;
}

