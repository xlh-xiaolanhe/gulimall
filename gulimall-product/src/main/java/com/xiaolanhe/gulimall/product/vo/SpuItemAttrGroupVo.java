package com.xiaolanhe.gulimall.product.vo;

import lombok.Data;

import java.util.List;

/**
 *@author: xiaolanhe
 *@createDate: 2022/6/9 22:03
 */

@Data
public class SpuItemAttrGroupVo {
    private String groupName;

    private List<Attr> attrs;
}
