package com.xianhuo.xianhuobackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xianhuo.xianhuobackend.common.ResponseProcess;
import com.xianhuo.xianhuobackend.common.ResponseResult;
import com.xianhuo.xianhuobackend.entity.Product;
import com.xianhuo.xianhuobackend.entity.SellMode;
import com.xianhuo.xianhuobackend.entity.SellModeDispatchRequire;
import com.xianhuo.xianhuobackend.service.DispatchModeService;
import com.xianhuo.xianhuobackend.service.ProductService;
import com.xianhuo.xianhuobackend.service.SellModeDIspatchRequireService;
import com.xianhuo.xianhuobackend.service.SellModeService;
import com.xianhuo.xianhuobackend.utils.JWTUtil;
import jdk.nashorn.internal.objects.annotations.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.support.HttpRequestHandlerServlet;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private SellModeDIspatchRequireService sellModeDIspatchRequireService;
    @Autowired
    private HttpServletRequest httpServletRequest;

    //    根据用户id获取已发布的商品
    @GetMapping("/product")
    public ResponseResult<List<Product>> allProductByUserId(Long id) {
        List<Product> list = productService.list(new LambdaQueryWrapper<Product>()
                .eq(Product::getUserId, id)
                .eq(Product::getStatus,1));
        return ResponseProcess.returnList(list);
    }

    //    根据商品ID获取商品全部信息
    @GetMapping("/product/{productId}")
    public ResponseResult<Product> productById(@PathVariable("productId") Long productId) {
        Product product = productService.getById(productId);
        return ResponseProcess.returnObject(product);
    }

    //根据售卖模式获取商品,分页获取
    @GetMapping("/product/sellMode/{sellModeId}")
    public ResponseResult productBySellMode(@PathVariable("sellModeId") Long sellModeId, Long current, Long size, String location) {
        Page<Product> page = new Page<>(current, size);
        Page<Product> paged = productService.page(page, new LambdaQueryWrapper<Product>()
                .eq(Product::getSellModeId, sellModeId)
                .eq(Product::getLocation, location)
                .eq(Product::getStatus, 1));
        return ResponseProcess.returnObject(paged);
    }

    // 分页获取最新的数据
    @GetMapping("/product/latest")
    public ResponseResult latestProducts(Long current, Long size, String location) {
        Page<Product> page = new Page<>(current, size);
        page.setMaxLimit(size);
        Page<Product> paged = productService.page(page, new LambdaQueryWrapper<Product>()
                .eq(Product::getLocation, location)
                .orderByDesc(Product::getCreateTime)
                .eq(Product::getStatus, 1));
        return ResponseProcess.returnObject(paged);
    }

    //    根据分类分页获取数据
    @GetMapping("/product/category/{categoryId}")
    public ResponseResult pageCategory(@PathVariable("categoryId") Long categoryId, Long current, Long size, String location) {
        Page<Product> page = new Page<>(current, size);
        page.setMaxLimit(size);
        Page<Product> paged = productService.page(page, new LambdaQueryWrapper<Product>()
                .eq(Product::getCategoryId, categoryId)
                .eq(Product::getLocation, location)
                .eq(Product::getStatus, 1));
        return ResponseProcess.returnObject(paged);
    }

    //    发布商品
    @PostMapping("/product")
    public ResponseResult increaseProduct(@RequestBody Product product) {
        long pId = new Date().getTime();
        product.setId(pId);
        boolean saved = productService.save(product);
        if(saved){
            return ResponseResult.ok(pId,"发布成功");
        }
        return ResponseResult.fail(null,"发布失败");
    }

    //    更新商品
    @PutMapping("/product")
    public ResponseResult updateProduct(@RequestBody Product product) {
        boolean updated = productService.updateById(product);
        return ResponseProcess.returnString(updated, "更新成功", "更新失败");
    }

    //    标记商品已经下架(-1)或售出(0)
    @DeleteMapping("/product/{productId}/{status}")
    public ResponseResult deleteProduct(@PathVariable("productId") Long id, @PathVariable("status") Long status) {
        boolean updated = productService.update(new LambdaUpdateWrapper<Product>()
                .set(Product::getStatus, status)
                .eq(Product::getId, id));
        return ResponseProcess.returnString(updated, "操作成功", "操作失败");
    }

    // 获取已经发布的商品
    @GetMapping("/product/released")
    public ResponseResult productReleased() {
        String authorization = httpServletRequest.getHeader("authorization");
        String id = JWTUtil.parseJWT(authorization).getId();
        List<Product> list = productService.list(new LambdaQueryWrapper<Product>()
                .eq(Product::getUserId, id)
                .eq(Product::getStatus, 1));
        return ResponseProcess.returnList(list);
    }

    //    获取发布商品数量
    @GetMapping("/product/count")
    public ResponseResult productCount() {
        String authorization = httpServletRequest.getHeader("authorization");
        String id = JWTUtil.parseJWT(authorization).getId();
        long count = productService.count(new LambdaQueryWrapper<Product>()
                .eq(Product::getUserId, id)
                .eq(Product::getStatus, 1));
        return ResponseResult.ok(count,"success");
    }

    //    获取商品要求对应的规则
    @GetMapping("/modes")
    public ResponseResult allModes() {
        List<SellModeDispatchRequire> list = sellModeDIspatchRequireService.allMode();
        return ResponseProcess.returnList(list);
    }

    //    分页获取搜索商品
    @GetMapping("/product/search")
    public ResponseResult searchProduct(Long current, Long size, String productName, String location, Long sellMode, Long categoryId) {
        Page<Product> page = new Page<>(current, size);
        Page<Product> paged = productService.page(page, new LambdaQueryWrapper<Product>()
                .eq(!Objects.equals(location, ""), Product::getLocation, location)
                .eq(Product::getStatus, 1)
                .like(!Objects.equals(productName, ""), Product::getDetail, productName)
                .eq(sellMode != 0, Product::getSellModeId, sellMode)
                .eq(categoryId != 0, Product::getCategoryId, categoryId));
        return ResponseProcess.returnObject(paged);
    }
}
