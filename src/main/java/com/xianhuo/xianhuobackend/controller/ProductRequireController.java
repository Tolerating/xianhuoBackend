package com.xianhuo.xianhuobackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xianhuo.xianhuobackend.common.ResponseProcess;
import com.xianhuo.xianhuobackend.common.ResponseResult;
import com.xianhuo.xianhuobackend.entity.ProductRequire;
import com.xianhuo.xianhuobackend.service.ProductRequireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class ProductRequireController {

    @Autowired
    private ProductRequireService productRequireService;

    //根据售卖方式和发货方式获取对应的商品要求
    @GetMapping("/productRequire/{sellId}/{dispatchId}")
    public ResponseResult<List<ProductRequire>> requireBySellAndDispatch(@PathVariable(value = "sellId") long sellId, @PathVariable(value = "dispatchId") int dispatchId) {
        List<ProductRequire> list = productRequireService.requireBySellAndDispatch(sellId, dispatchId);
        return ResponseProcess.returnList(list);
    }

//    返回所有有效的商品要求

    @GetMapping("/productRequire")
    public ResponseResult allUseableProductRequire() {
        LambdaQueryWrapper<ProductRequire> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductRequire::getStatus, 1);
        List<ProductRequire> list = productRequireService.list(wrapper);
        return ResponseProcess.returnList(list);
    }

    //    获取所有
    @GetMapping("/productRequires")
    public ResponseResult all(Long current, Long size) {
        Page<ProductRequire> page = new Page<ProductRequire>(current, size);
        Page<ProductRequire> productRequirePage = productRequireService.page(page);
        return ResponseProcess.returnObject(productRequirePage);
    }

    //    改动商品要求是否生效
    @GetMapping("/productRequire/update")
    public ResponseResult updateRequireStatus(Long id, Integer status) {
        boolean update = productRequireService.update(new LambdaUpdateWrapper<ProductRequire>()
                .set(ProductRequire::getStatus, status)
                .set(ProductRequire::getUpdateTime, new Date())
                .eq(ProductRequire::getId, id));
        return ResponseProcess.returnString(update, "操作成功", "操作失败");
    }

    //    新增商品要求
    @GetMapping("/productRequire/add")
    public ResponseResult addProRequire(String name) {
        ProductRequire one = productRequireService.getOne(new LambdaQueryWrapper<ProductRequire>()
                .eq(ProductRequire::getName, name));
        if(one==null){
            ProductRequire require = new ProductRequire();
            require.setName(name);
            boolean saved = productRequireService.save(require);
            return ResponseProcess.returnString(saved, "添加成功", "添加失败");
        }
        return ResponseResult.fail("101","请勿添加重复的商品要求");
    }

}
