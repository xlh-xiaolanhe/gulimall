package com.xiaolanhe.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaolanhe.common.utils.PageUtils;
import com.xiaolanhe.gulimall.product.entity.CommentReplayEntity;

import java.util.Map;

/**
 * 商品评价回复关系
 *
 * @author xiaolanhe@163.com
 * @email sunlightcs@gmail.com
 * @date 2022-04-26 22:46:33
 */
public interface CommentReplayService extends IService<CommentReplayEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

