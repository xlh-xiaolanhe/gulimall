package com.xiaolanhe.gulimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaolanhe.common.utils.PageUtils;
import com.xiaolanhe.gulimall.coupon.entity.SeckillPromotionEntity;

import java.util.Map;

/**
 * 秒杀活动
 *
 * @author xiaolanhe@163.com
 * @email sunlightcs@gmail.com
 * @date 2022-05-15 21:40:39
 */
public interface SeckillPromotionService extends IService<SeckillPromotionEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

