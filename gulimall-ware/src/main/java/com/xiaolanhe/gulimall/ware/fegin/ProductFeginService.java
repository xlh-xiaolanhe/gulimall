package com.xiaolanhe.gulimall.ware.fegin;

import com.xiaolanhe.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author xiaolanhe
 * @version V1.0
 * @Package com.xiaolanhe.gulimall.ware.fegin
 * @date 2022/5/21 0:17
 */

@FeignClient("gulimall-product")
public interface ProductFeginService {

    @RequestMapping("/product/skuinfo/info/{skuId}")
    public R info(@PathVariable("skuId") Long skuId);
}
