package com.xiaolanhe.gulimall.coupon.dao;

import com.xiaolanhe.gulimall.coupon.entity.CouponSpuRelationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券与产品关联
 * 
 * @author xiaolanhe@163.com
 * @email sunlightcs@gmail.com
 * @date 2022-05-15 21:40:39
 */
@Mapper
public interface CouponSpuRelationDao extends BaseMapper<CouponSpuRelationEntity> {
	
}
