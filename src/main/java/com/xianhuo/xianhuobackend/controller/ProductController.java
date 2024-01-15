package com.xianhuo.xianhuobackend.controller;

import com.xianhuo.xianhuobackend.common.ResponseProcess;
import com.xianhuo.xianhuobackend.common.ResponseResult;
import com.xianhuo.xianhuobackend.entity.Product;
import com.xianhuo.xianhuobackend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/product")
    public List<Product> allProduct(){
        return productService.list();
    }

    @PostMapping("/product")
    public ResponseResult increaseProduct(@RequestBody Product product){
        boolean saved = productService.save(product);
        return ResponseProcess.returnString(saved,"发布成功","发布失败");
    }
}
