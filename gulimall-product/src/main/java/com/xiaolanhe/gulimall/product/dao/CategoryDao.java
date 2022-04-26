package com.xiaolanhe.gulimall.product.dao;

import com.xiaolanhe.gulimall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author xiaolanhe@163.com
 * @email sunlightcs@gmail.com
 * @date 2022-04-26 22:46:33
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
