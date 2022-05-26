package com.xiaolanhe.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaolanhe.common.utils.PageUtils;
import com.xiaolanhe.gulimall.product.entity.SpuInfoDescEntity;
import com.xiaolanhe.gulimall.product.entity.SpuInfoEntity;
import com.xiaolanhe.gulimall.product.vo.SpuSaveVo;

import java.util.Map;

/**
 * spu信息
 *
 * @author xiaolanhe@163.com
 * @email sunlightcs@gmail.com
 * @date 2022-04-26 22:46:33
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSpuInfo(SpuSaveVo vo);

    void saveBaseSpuInfo(SpuInfoEntity spuInfoEntity);

    PageUtils queryPageByCondition(Map<String, Object> params);

    void up(Long spuId);
}

