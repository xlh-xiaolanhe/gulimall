package com.xiaolanhe.gulimall.search.controller;

import com.xiaolanhe.common.exception.BizCodeEnum;
import com.xiaolanhe.common.to.es.SkuEsModel;
import com.xiaolanhe.common.utils.R;
import com.xiaolanhe.gulimall.search.service.ProductSaveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 *@author: xiaolanhe
 *@createDate: 2022/5/26 21:29
 */

@Slf4j
@RestController
@RequestMapping("/search")
public class ElasticSaveController {

    @Autowired
    ProductSaveService productSaveService;

    // 上架商品
    @PostMapping("/product")
    public R productStatusUp(@RequestBody List<SkuEsModel> skuEsModels){
        boolean res = false;
        try {
            res = productSaveService.productStatusUp(skuEsModels);
        } catch (IOException e) {
            log.error("ElasticSaveController商品上架错误:{}", e);
            return R.error(BizCodeEnum.PRODUCT_UP_EXCEPTION.getCode(), BizCodeEnum.PRODUCT_UP_EXCEPTION.getMsg());
        }
        if(!res){
            return R.ok();
        }

        return R.error(BizCodeEnum.PRODUCT_UP_EXCEPTION.getCode(), BizCodeEnum.PRODUCT_UP_EXCEPTION.getMsg());
    }
}
