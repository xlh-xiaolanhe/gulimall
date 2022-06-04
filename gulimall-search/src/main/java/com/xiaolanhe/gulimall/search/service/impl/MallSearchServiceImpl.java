package com.xiaolanhe.gulimall.search.service.impl;

import com.xiaolanhe.gulimall.search.config.GulimallElasticsearchConfig;
import com.xiaolanhe.gulimall.search.constant.EsConstant;
import com.xiaolanhe.gulimall.search.service.MallSearchService;
import com.xiaolanhe.gulimall.search.vo.SearchParam;
import com.xiaolanhe.gulimall.search.vo.SearchResult;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.swing.*;
import java.io.IOException;

/**
 *@author: xiaolanhe
 *@createDate: 2022/6/4 19:07
 */

@Service
public class MallSearchServiceImpl implements MallSearchService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Override
    public SearchResult search(SearchParam param) {
        // 动态构建出查询需要的DSL语句
        SearchResult result = null;

        // 1.创建检索请求
        SearchRequest request = buildSearchQuest(param);

        try{
            // 2. 执行检索请求
            SearchResponse res = restHighLevelClient.search(request, GulimallElasticsearchConfig.COMMON_OPTIONS);

            // 3. 分析响应数据封装成我们需要的格式
            result = buildSearchResult(res);
        }catch (IOException e){
            e.printStackTrace();
        }

        return result;
    }

    // 构建结果数据
    private SearchResult buildSearchResult(SearchResponse response){
        return null;
    }


    // 准备检索请求
    // 模糊匹配，过滤（按照属性、分类、品牌，价格区间，库存），完成排序、分页、高亮,聚合分析功能
    private SearchRequest buildSearchQuest(SearchParam param){

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        /*查询:  模糊匹配，过滤（按照属性、分类、品牌，价格区间，库存）*/

          // 1. 构建boolquery
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
            // 1.1 must-模糊匹配
        if(!StringUtils.isEmpty(param.getKeyWord())){
            queryBuilder.must(QueryBuilders.matchQuery("skuTitle", param.getKeyWord()));
        }

            // 1.2 filter
        // 按照三级分类id查询
        if(null != param.getCatalog3Id()){
            queryBuilder.filter(QueryBuilders.termQuery("catalogId", param.getCatalog3Id()));
        }

        // 按照品牌id查询
        if(null != param.getBrandId() && param.getBrandId().size() > 0){
            queryBuilder.filter(QueryBuilders.termQuery("brandId", param.getBrandId()));
        }

        // 按照是否有库存进行查询
        queryBuilder.filter(QueryBuilders.termQuery("hasStock", param.getHasSrock() == 1));

        // 按照所有指定属性进行查询
        if(null != param.getAttrs() && param.getAttrs().size() > 0){


            for(String attrStr : param.getAttrs())
            {
                BoolQueryBuilder nestedboolQuery = QueryBuilders.boolQuery();
                String[] s = attrStr.split("_");
                // 检索的属性Id
                String attrId = s[0];

                // 该属性用于检索的值
                String[] attrValues = s[1].split(":");
                nestedboolQuery.must(QueryBuilders.termQuery("attrs.attrId", attrId));
                nestedboolQuery.must(QueryBuilders.termQuery("attrs.attrValue", attrValues));

                // 每一个需要检索的属性都需要生成一个nested查询
                NestedQueryBuilder nestedQuery = QueryBuilders.nestedQuery("attrs", null, ScoreMode.None);
                queryBuilder.filter(nestedQuery);
            }


        }

        // 按照价格区间进行查询
        if(!StringUtils.isEmpty(param.getSkuPrice()))
        {
            // 1_500 表示 1到 500

            /*
                "range":{
                    "skuPrice": {
                        "gte": 0,
                        "lte": 6000
                    }
                 }
            */
            RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("skuPrice");
            String[] s = param.getSkuPrice().split("_");
            if(s.length == 2){
                rangeQuery.gte(s[0]).lte(s[1]);
            }else if(s.length == 1){
                if(param.getSkuPrice().charAt(0) == '_'){
                    rangeQuery.lte(s[0]);
                }else {
                    rangeQuery.gte(s[0]);
                }
            }
        }

        //  把上面的所有条件都拿来进行封装
        sourceBuilder.query(queryBuilder);

        /*完成排序、分页、高亮*/

        // 2.1 排序
        if(!StringUtils.isEmpty(param.getSort()))
        {
            String sort = param.getSort();
            // sort=hotScore_desc/asc
            String[] s = sort.split("_");
            SortOrder ORDER = s[1].equalsIgnoreCase("asc") ? SortOrder.ASC : SortOrder.DESC;
            sourceBuilder.sort(s[0], ORDER);
        }

        // 2.2 分页
        sourceBuilder.from((param.getPageNum()-1) * EsConstant.PRODUCT_PAGESIZE);
        sourceBuilder.size(EsConstant.PRODUCT_PAGESIZE);

        // 2.3 高亮
        if(!StringUtils.isEmpty(param.getKeyWord()))
        {
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field("skuTitle");
            highlightBuilder.preTags("<b style='color:red'>");
            highlightBuilder.postTags("</b>");
            sourceBuilder.highlighter(highlightBuilder);
        }

        /* 聚合分析功能 */
         // 1. 品牌聚合
        TermsAggregationBuilder brand_agg = AggregationBuilders.terms("brand_agg");
        brand_agg.field("brandId").size(50);

        // 品牌聚合的子聚合
        brand_agg.subAggregation(AggregationBuilders.terms("brand_name_agg").field("brandName").size(1));
        brand_agg.subAggregation(AggregationBuilders.terms("brand_img_agg").field("brandImg").size(1));

        sourceBuilder.aggregation(brand_agg);

        //  2. 分类聚合
        TermsAggregationBuilder catalog_agg = AggregationBuilders.terms("catalog_agg").field("catalogId").size(20);
        catalog_agg.subAggregation(AggregationBuilders.terms("catalog_name_agg").field("catalogName").size(20));
        sourceBuilder.aggregation(catalog_agg);

        // 3. 属性聚合
        NestedAggregationBuilder attr_agg = AggregationBuilders.nested("attr_agg", "attrs");
        // 聚合出所有的attrId
        TermsAggregationBuilder attr_id_agg = AggregationBuilders.terms("attr_id_agg").field("attrs.attrId");

        // 聚合分析出当前 attr_id 对应的名字
        attr_id_agg.subAggregation(AggregationBuilders.terms("attr_name_agg").field("attrs.attrName"));
        // 聚合分析出当前 attr_id 对应的所有可能的属性值
        attr_id_agg.subAggregation(AggregationBuilders.terms("attr_value_agg").field("attrs.attrValues"));

        attr_agg.subAggregation(attr_id_agg);
        sourceBuilder.aggregation(attr_agg);

        SearchRequest searchRequest = new SearchRequest(new String[]{EsConstant.PRODUCT_INDEX}, sourceBuilder);

        return searchRequest;
    }
}
