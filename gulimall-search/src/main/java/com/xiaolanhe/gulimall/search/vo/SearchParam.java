package com.xiaolanhe.gulimall.search.vo;

import lombok.Data;

import java.util.List;

/**
 *@author: xiaolanhe
 *@createDate: 2022/6/4 19:05
 */


// 封装页面所有可能传来的查询条件
    /**
     catalog3Id=225  keyword=21312  排序条件sort
    */

@Data
public class SearchParam {

    private String keyWord; // 页面传来的全文搜索关键字

    private Long catalog3Id; // 三级分类id

    private String sort; // 排序条件 (销量 saleCount_asce/desc  价格、热度评分 (三选一))

    // 过滤条件 hasStock=0/1(是否有货)  skuPrice(价格区间)
    private Integer hasSrock;

    private String skuPrice; // 价格区间查询

    private List<Long> brandId; // 品牌id,可以多选

    private List<String> attrs; // 按照属性进行筛选

    private Integer pageNum; // 页码
}
