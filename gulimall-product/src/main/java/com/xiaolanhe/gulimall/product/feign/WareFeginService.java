package com.xiaolanhe.gulimall.product.feign;


import com.xiaolanhe.gulimall.product.vo.SkuHasStockVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.xiaolanhe.common.utils.R;

import java.util.List;

/**
 * @author xiaolanhe
 * @version V1.0
 * @Package com.xiaolanhe.gulimall.product.feign
 * @date 2022/5/26 21:05
 */

@FeignClient("gulimall-ware")
public interface WareFeginService {

    // 查询sku是否有库存
    @PostMapping("/ware/waresku/hasstock")
    R getSkuHasStock(@RequestBody List<Long> skuIds);
}
