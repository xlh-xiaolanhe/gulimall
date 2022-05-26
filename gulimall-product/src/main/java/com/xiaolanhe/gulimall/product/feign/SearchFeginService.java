package com.xiaolanhe.gulimall.product.feign;

import com.xiaolanhe.common.to.es.SkuEsModel;
import com.xiaolanhe.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author xiaolanhe
 * @version V1.0
 * @Package com.xiaolanhe.gulimall.product.feign
 * @date 2022/5/26 22:05
 */

@FeignClient("gulimall-search")
public interface SearchFeginService {

    @PostMapping("/search/product")
    public R productStatusUp(@RequestBody List<SkuEsModel> skuEsModels);
}
