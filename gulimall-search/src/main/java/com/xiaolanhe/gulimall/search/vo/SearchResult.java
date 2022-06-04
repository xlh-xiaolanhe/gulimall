package com.xiaolanhe.gulimall.search.vo;

import com.xiaolanhe.common.to.es.SkuEsModel;
import lombok.Data;

import java.util.List;

/**
 *@author: xiaolanhe
 *@createDate: 2022/6/4 19:42
 */

@Data
public class SearchResult {

    private List<SkuEsModel> products; // 查询到的所有商品信息

    // 下面是分页信息
    private Integer pageNum; // 当前页码

    private Long total; // 总记录数

    private Integer totalPages; // 总页码

    private List<BrandVo> brandVos; // 查询到的结果所涉及的品牌

    private List<AttrVo> attrVos; // 查询到的结果所涉及到的属性

    private List<CatalogVo> catalogVos; // 查询到的结果所涉及到的分类

    // 以上是返回给页面的信息

    @Data
    public static class CatalogVo{
        private Long catalogId;

        private String catalogName;

    }

    @Data
    public static class BrandVo{
        private Long brandId;

        private String brandName;

        private String brandImg;
    }

    @Data
    public static class AttrVo{
        private Long attrId;

        private String attrName;

        private List<String> attrValues;
    }
}
