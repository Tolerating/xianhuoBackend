package com.xianhuo.xianhuobackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xianhuo.xianhuobackend.common.ResponseProcess;
import com.xianhuo.xianhuobackend.common.ResponseResult;
import com.xianhuo.xianhuobackend.entity.DispatchMode;
import com.xianhuo.xianhuobackend.entity.SellModeDispatchRequire;
import com.xianhuo.xianhuobackend.service.DispatchModeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class DispatchModeController {

    @Autowired
    private DispatchModeService dispatchModeService;

    //根据售卖模式id查询可用的发货方式
    @GetMapping("/dispatchMode/{id}")
    public ResponseResult<List<DispatchMode>> getUseableModeBySellId(@PathVariable(value = "id") long id){
        List<DispatchMode> mode = dispatchModeService.getUsableDispatchModeBySellId(id);
        return ResponseProcess.returnList(mode);
    }
}
