package com.xianhuo.xianhuobackend.controller;

import com.xianhuo.xianhuobackend.common.ResponseResult;
import com.xianhuo.xianhuobackend.entity.XhCategory;
import com.xianhuo.xianhuobackend.service.GoodsService;
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
    private GoodsService goodsService;

    @GetMapping("/categories")
    public ResponseResult<List<XhCategory>> categoryList(){
        List<XhCategory> xhCategories = goodsService.allCategory();

        return ResponseResult.ok(xhCategories,"获取成功");
    }

}
