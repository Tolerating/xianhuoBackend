package com.xianhuo.xianhuobackend.controller;

import com.xianhuo.xianhuobackend.common.ResponseProcess;
import com.xianhuo.xianhuobackend.common.ResponseResult;
import com.xianhuo.xianhuobackend.entity.ProductRequire;
import com.xianhuo.xianhuobackend.service.ProductRequireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class ProductRequireController {

    @Autowired
    private ProductRequireService productRequireService;

    //根据售卖方式和发货方式获取对应的商品要求
    @GetMapping("/productRequire/{sellId}/{dispatchId}")
    public ResponseResult<List<ProductRequire>> requireBySellAndDispatch(@PathVariable(value = "sellId") long sellId, @PathVariable(value = "dispatchId") int dispatchId){
        List<ProductRequire> list = productRequireService.requireBySellAndDispatch(sellId, dispatchId);
        return ResponseProcess.returnList(list);
    }
}
