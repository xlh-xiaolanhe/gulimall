package com.xiaolanhe.gulimall.ware.Vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *@author: xiaolanhe
 *@createDate: 2022/5/20 23:18
 */

@Data
public class PurchaseDoneVo {

    @NotNull
    private Long id; // 采购单id

    private List<PurchaseItemDoneVo> items;
}
