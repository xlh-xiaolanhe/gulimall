package com.xiaolanhe.gulimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaolanhe.common.to.SkuReductionTo;
import com.xiaolanhe.common.utils.PageUtils;
import com.xiaolanhe.gulimall.coupon.entity.SkuFullReductionEntity;

import java.util.Map;

/**
 * 商品满减信息
 *
 * @author xiaolanhe@163.com
 * @email sunlightcs@gmail.com
 * @date 2022-05-15 21:40:39
 */
public interface SkuFullReductionService extends IService<SkuFullReductionEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSkuReduction(SkuReductionTo reductionTo);
}

