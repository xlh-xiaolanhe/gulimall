package com.xiaolanhe.gulimall.search.fegin;

import com.xiaolanhe.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author xiaolanhe
 * @version V1.0
 * @Package com.xiaolanhe.gulimall.search.fegin
 * @date 2022/6/7 22:07
 */

@FeignClient("gulimall-product")
public interface ProductFeginService {

    @GetMapping("/product/attr/info/{attrId}")
    //@RequiresPermissions("product:attr:info")
    public R attrInfo(@PathVariable("attrId") Long attrId);

    @GetMapping("/product/brand/infos")
    public R brandInfo(@RequestParam("brandIds") List<Long> brandId);
}
