package com.xiaolanhe.gulimall.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.xiaolanhe.common.to.es.SkuEsModel;
import com.xiaolanhe.gulimall.search.config.GulimallElasticsearchConfig;
import com.xiaolanhe.gulimall.search.constant.EsConstant;
import com.xiaolanhe.gulimall.search.service.ProductSaveService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 *@author: xiaolanhe
 *@createDate: 2022/5/26 21:34
 */

@Slf4j
@Service
public class ProductSaveServiceImpl implements ProductSaveService {

    @Autowired
    RestHighLevelClient restHighLevelClient;

    @Override
    public boolean productStatusUp(List<SkuEsModel> skuEsModels) throws IOException {

        // 1. 给es中建立索引 product 建立好映射关系

        // 2. 给es中保存这些数据
        // BulkRequest    RequestOptions options
        BulkRequest bulkRequest = new BulkRequest();
        for(SkuEsModel model : skuEsModels)
        {
            // 1. 构造保存请求
            IndexRequest indexRequest = new IndexRequest((EsConstant.PRODUCT_INDEX));
            indexRequest.id(model.getSkuId().toString());
            String s = JSON.toJSONString(model);
            indexRequest.source(s, XContentType.JSON);

            bulkRequest.add(indexRequest);
        }

        BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, GulimallElasticsearchConfig.COMMON_OPTIONS);

        // TODO 如果批量错误
        boolean result = bulk.hasFailures();
        List<String> collect = Arrays.stream(bulk.getItems()).map(item -> {
            return item.getId();
        }).collect(Collectors.toList());

        log.error("商品上架成功:{}", collect);

        return result;
    }
}
