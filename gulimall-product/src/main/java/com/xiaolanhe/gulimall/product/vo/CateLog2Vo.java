package com.xiaolanhe.gulimall.product.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 *@author: xiaolanhe
 *@createDate: 2022/5/28 15:27
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CateLog2Vo {

    private String cataelogId; // 1级父分类 id

    private List<Catelog3Vo> catalog3List; // 三级子分类

    private String id;

    private String name;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Catelog3Vo{
        private String catalog2Id; //父分类id

        private String id;

        private String name;
    }

}
