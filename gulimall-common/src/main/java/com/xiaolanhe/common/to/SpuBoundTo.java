package com.xiaolanhe.common.to;

import lombok.Data;

import java.math.BigDecimal;

/**
 *@author: xiaolanhe
 *@createDate: 2022/5/18 18:06
 */

@Data
public class SpuBoundTo {

    private Long spuId;

    private BigDecimal buyBounds;
    private BigDecimal growBounds;

}
