package com.xiaolanhe.gulimall.product.feign;


import com.xiaolanhe.common.to.SkuReductionTo;
import com.xiaolanhe.common.to.SpuBoundTo;
import com.xiaolanhe.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author xiaolanhe
 * @version V1.0
 * @Package com.xiaolanhe.gulimall.product.feign
 * @date 2022/5/18 18:02
 */

@FeignClient("gulimall-coupon") // 指定调用哪个服务
public interface CouponFeginService {

    /**
     CouponFeginService.saveSpuBounds(spuBoundTo)的调用过程
        1.@RequestBody将这个对象转为json格式
        2.找到 gulimall-coupon 服务，给/coupon/spubounds/save发送请求
            将上一步的json数据放在请求体位置，发送请求。
        3.对方服务收到请求，请求体里有json数据
            (@RequestBody SpuBoundsEntity spuBounds): 将请求体的json转为SpuBoundsEntity;

     结论: 只要json数据模型是兼容的(key相同)，双发服务无需使用同一个 to
    */

    @PostMapping("/coupon/spubounds/save")
    R saveSpuBounds(@RequestBody SpuBoundTo spuBoundTo);

    @PostMapping("/coupon/skufullreduction/saveInfo")
    R saveSkuReduction(@RequestBody SkuReductionTo skuReductionTo);
}
