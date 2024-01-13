package com.xianhuo.xianhuobackend.controller;

import com.xianhuo.xianhuobackend.common.ResponseProcess;
import com.xianhuo.xianhuobackend.common.ResponseResult;
import com.xianhuo.xianhuobackend.entity.Category;
import com.xianhuo.xianhuobackend.entity.Product;
import com.xianhuo.xianhuobackend.service.CategoryService;
import com.xianhuo.xianhuobackend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    //获取全部分类
    @GetMapping("/categories")
    public ResponseResult<List<Category>> categoryList(){
        List<Category> categories = categoryService.list();
        return ResponseProcess.returnList(categories);
    }

}
