package com.xiaolanhe.gulimall.member.feign;

import com.xiaolanhe.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author xiaolanhe
 * @version V1.0
 * @Package com.xiaolanhe.gulimall.member.feign
 * @date 2022/5/15 22:01
 */

@FeignClient("gulimall-coupon")
public interface CouponFeginService {

    @RequestMapping("/coupon/coupon/member/list")
    public R membercoupons();
}
