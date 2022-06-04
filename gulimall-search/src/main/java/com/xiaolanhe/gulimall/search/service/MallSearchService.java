package com.xiaolanhe.gulimall.search.service;


import com.xiaolanhe.gulimall.search.vo.SearchParam;
import com.xiaolanhe.gulimall.search.vo.SearchResult;

/**
 *@author: xiaolanhe
 *@createDate: 2022/6/4 19:06
 */


public interface MallSearchService {

    /**
     * desciption
      * @param param 检索的所有参数
     * @return 返回检索的结果
    */
    SearchResult search(SearchParam param);
}
