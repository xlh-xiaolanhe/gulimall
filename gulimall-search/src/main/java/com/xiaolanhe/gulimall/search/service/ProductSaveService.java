package com.xiaolanhe.gulimall.search.service;

import com.xiaolanhe.common.to.es.SkuEsModel;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 *@author: xiaolanhe
 *@createDate: 2022/5/26 21:32
 */

public interface ProductSaveService {
    boolean productStatusUp(List<SkuEsModel> skuEsModels) throws IOException;
}
