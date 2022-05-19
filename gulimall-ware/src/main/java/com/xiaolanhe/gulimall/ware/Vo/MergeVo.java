package com.xiaolanhe.gulimall.ware.Vo;

import lombok.Data;

import java.util.List;

/**
 *@author: xiaolanhe
 *@createDate: 2022/5/19 23:14
 */

@Data
public class MergeVo {

   /* {
        purchaseId: 1, //整单id
                items:[1,2,3,4] //合并项集合
    }*/

    private Long purchaseId;
    private List<Long> items;

}
