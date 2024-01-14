package com.xianhuo.xianhuobackend.controller;

import com.xianhuo.xianhuobackend.common.ResponseProcess;
import com.xianhuo.xianhuobackend.common.ResponseResult;
import com.xianhuo.xianhuobackend.entity.SellMode;
import com.xianhuo.xianhuobackend.service.ProductService;
import com.xianhuo.xianhuobackend.service.SellModeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class SellModeController {
    @Autowired
    private SellModeService sellModeService;

    @GetMapping("/sellMode/{status}")
    public ResponseResult<List<SellMode>> allSellModeByStatus(@PathVariable(value = "status")int status){
        List<SellMode> list = sellModeService.list()
                .stream()
                .filter(e->e.getStatus().equals(status))
                .collect(Collectors.toList());
        return ResponseProcess.returnList(list);
    }
}
