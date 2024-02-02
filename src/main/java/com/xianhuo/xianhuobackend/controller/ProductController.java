package com.xianhuo.xianhuobackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xianhuo.xianhuobackend.common.ResponseProcess;
import com.xianhuo.xianhuobackend.common.ResponseResult;
import com.xianhuo.xianhuobackend.entity.Product;
import com.xianhuo.xianhuobackend.service.ProductService;
import com.xianhuo.xianhuobackend.utils.JWTUtil;
import jdk.nashorn.internal.objects.annotations.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.support.HttpRequestHandlerServlet;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private HttpServletRequest httpServletRequest;

    @GetMapping("/product")
    public List<Product> allProduct(){
        return productService.list();
    }
//    根据商品ID获取商品全部信息
    @GetMapping("/product/{productId}")
    public ResponseResult<Product> productById(@PathVariable("productId") Long productId){
        Product product = productService.getById(productId);
        return ResponseProcess.returnObject(product);
    }

    //根据售卖模式获取商品
    @GetMapping("/product/sellMode/{sellModeId}")
    public ResponseResult productBySellMode(Long sellModeId){
//        productService.list
        return null;
    }

//    发布商品
    @PostMapping("/product")
    public ResponseResult increaseProduct(@RequestBody Product product){
        boolean saved = productService.save(product);
        return ResponseProcess.returnString(saved,"发布成功","发布失败");
    }
// 获取已经发布的商品
    @GetMapping("/product/released")
    public ResponseResult productReleased(){
        String authorization = httpServletRequest.getHeader("authorization");
        String id = JWTUtil.parseJWT(authorization).getId();
        List<Product> list = productService.list(new LambdaQueryWrapper<Product>().eq(Product::getUserId, id));
        return ResponseProcess.returnList(list);
    }

//    获取发布商品数量
    @GetMapping("/product/count")
    public ResponseResult productCount(){
        String authorization = httpServletRequest.getHeader("authorization");
        String id = JWTUtil.parseJWT(authorization).getId();
        long count = productService.count(new LambdaQueryWrapper<Product>().eq(Product::getUserId, id));
        return ResponseProcess.returnLong(count,"success","fail");
    }
}
