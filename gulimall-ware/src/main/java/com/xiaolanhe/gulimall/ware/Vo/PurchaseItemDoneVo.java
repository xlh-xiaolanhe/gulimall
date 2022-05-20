package com.xiaolanhe.gulimall.ware.Vo;

import lombok.Data;

/**
 *@author: xiaolanhe
 *@createDate: 2022/5/20 23:15
 */

@Data
public class PurchaseItemDoneVo {

    private Long itemId;
    private Integer status;
    private String reason;
}
