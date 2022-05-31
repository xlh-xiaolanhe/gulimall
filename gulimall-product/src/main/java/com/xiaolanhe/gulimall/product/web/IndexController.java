package com.xiaolanhe.gulimall.product.web;

import com.xiaolanhe.gulimall.product.entity.CategoryEntity;
import com.xiaolanhe.gulimall.product.service.CategoryService;
import com.xiaolanhe.gulimall.product.vo.CateLog2Vo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 *@author: xiaolanhe
 *@createDate: 2022/5/27 22:43
 */

@Controller
public class IndexController {

    @Autowired
    CategoryService categoryService;

    @GetMapping({"/","/index.html"})
    public String indexPage(Model model)
    {
        // TODO 查出所有的 1 级分类
        List<CategoryEntity> categoryEntities = categoryService.getLevelOneCategorys();

        //视图解析器进行拼串  classpath: 代表resources
        //classpath:/templates/+返回值+ .html
        model.addAttribute("categorys", categoryEntities);
        return "index";
    }

    //  index/catalog.json
    @ResponseBody
    @GetMapping("/index/catalog.json")
    public Map<String, List<CateLog2Vo>> getCatalogJson()
    {
        Map<String, List<CateLog2Vo>> map = categoryService.getCatalogJson();
        return map;
    }

}
