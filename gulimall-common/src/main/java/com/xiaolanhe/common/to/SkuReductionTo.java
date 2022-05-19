package com.xiaolanhe.common.to;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 *@author: xiaolanhe
 *@createDate: 2022/5/18 18:53
 */
@Data
public class SkuReductionTo {

    private Long skuId;
    private int fullCount;
    private BigDecimal discount;
    private int countStatus;
    private BigDecimal fullPrice;
    private BigDecimal reducePrice;
    private int priceStatus;
    private List<MemberPrice> memberPrice;

}