package com.xiaolanhe.gulimall.search.controller;

import com.xiaolanhe.gulimall.search.service.MallSearchService;
import com.xiaolanhe.gulimall.search.vo.SearchParam;
import com.xiaolanhe.gulimall.search.vo.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

/**
 *@author: xiaolanhe
 *@createDate: 2022/6/4 17:05
 */

@Controller
public class SearchController {

    @Autowired
    MallSearchService mallSearchService;

    ///list.html?catalog3Id=225
    @GetMapping("/list.html")
    // 自动将查询参数封装成指定的对象
    public String listPage(SearchParam param, Model model, HttpServletRequest request)
    {
        String queryString = request.getQueryString();

        param.set_queryString(queryString);

        // 根据传递来的页面的查询参数，去es中检索商品
        SearchResult res = mallSearchService.search(param);
        model.addAttribute("result", res);
        return "list";
    }
}
