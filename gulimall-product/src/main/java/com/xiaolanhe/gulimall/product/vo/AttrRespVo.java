package com.xiaolanhe.gulimall.product.vo;

import lombok.Data;

/**
 *@author: xiaolanhe
 *@createDate: 2022/5/14 19:42
 */
@Data
public class AttrRespVo extends AttrVo{

    // "catelogName": "手机/数码/手机", 所属分类名字
    //  "groupName": "主体", 所属分组名字

    private String catelogName;

    private String groupName;

    private Long[] catelogPath;
}
