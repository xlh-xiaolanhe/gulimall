package com.xiaolanhe.gulimall.product.web;

import com.xiaolanhe.gulimall.product.service.SkuInfoService;
import com.xiaolanhe.gulimall.product.vo.SkuItemVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 *@author: xiaolanhe
 *@createDate: 2022/6/9 21:10
 */

@Controller
public class ItemController {

    @Autowired
    SkuInfoService skuInfoService;

    // 展示当前sku的详情
    @GetMapping("/{skuId}.html")
    public String skuItem(@PathVariable("skuId") Long skuId)
    {
        System.out.println("准备查询" + skuId + "详情");
        SkuItemVo vo = skuInfoService.item(skuId);
        return "item";
    }
}
