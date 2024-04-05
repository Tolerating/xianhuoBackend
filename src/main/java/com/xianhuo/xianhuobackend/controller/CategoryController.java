package com.xianhuo.xianhuobackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xianhuo.xianhuobackend.common.ResponseProcess;
import com.xianhuo.xianhuobackend.common.ResponseResult;
import com.xianhuo.xianhuobackend.entity.Category;
import com.xianhuo.xianhuobackend.entity.Product;
import com.xianhuo.xianhuobackend.entity.ProductRequire;
import com.xianhuo.xianhuobackend.service.CategoryService;
import com.xianhuo.xianhuobackend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    //获取全部分类
    @GetMapping("/categories")
    public ResponseResult<List<Category>> categoryList(){
        List<Category> categories = categoryService.list();
        return ResponseProcess.returnList(categories);
    }

    //    分页获取商品分类
    @GetMapping("/categories/page")
    public ResponseResult all(Long current, Long size) {
        Page<Category> page = new Page<>(current, size);
        Page<Category> productRequirePage = categoryService.page(page);
        return ResponseProcess.returnObject(productRequirePage);
    }

    //    改动商品分类是否生效
    @GetMapping("/category/update")
    public ResponseResult updateRequireStatus(Long id, Integer status) {
        boolean update = categoryService.update(new LambdaUpdateWrapper<Category>()
                .set(Category::getStatus, status)
                .set(Category::getUpdateTime, new Date())
                .eq(Category::getId, id));
        return ResponseProcess.returnString(update, "操作成功", "操作失败");
    }

    //    新增商品要求
    @GetMapping("/category/add")
    public ResponseResult addProRequire(String name) {
        Category one = categoryService.getOne(new LambdaQueryWrapper<Category>()
                .eq(Category::getName, name));
        if(one==null){
            Category require = new Category();
            require.setName(name);
            boolean saved = categoryService.save(require);
            return ResponseProcess.returnString(saved, "添加成功", "添加失败");
        }
        return ResponseResult.fail("101","请勿添加重复的商品要求");
    }

}
