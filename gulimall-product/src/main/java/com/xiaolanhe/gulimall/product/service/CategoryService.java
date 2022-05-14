package com.xiaolanhe.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaolanhe.common.utils.PageUtils;
import com.xiaolanhe.gulimall.product.entity.CategoryEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author xiaolanhe@163.com
 * @email sunlightcs@gmail.com
 * @date 2022-04-26 22:46:33
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<CategoryEntity> listWithTree();

    void removeMenueByIds(List<Long> asList);

    /**
     * desciption: 找到catelogId的完整路径 [父|子|孙]
      * @param catelogId
     * @return java.lang.Long[]
     * @Date 2022/5/9 21:48
    */
    Long[] findCatelogPath(Long catelogId);

    void updateCascade(CategoryEntity category);
}

