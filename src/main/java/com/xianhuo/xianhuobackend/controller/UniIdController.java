package com.xianhuo.xianhuobackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xianhuo.xianhuobackend.common.ResponseProcess;
import com.xianhuo.xianhuobackend.common.ResponseResult;
import com.xianhuo.xianhuobackend.entity.UniId;
import com.xianhuo.xianhuobackend.service.UniIdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class UniIdController {
    @Autowired
    private UniIdService uniIdService;
    @PostMapping("/uniId/add")
    public ResponseResult addUniId(@RequestBody UniId uniId){
        UniId one = uniIdService.getOne(new LambdaQueryWrapper<UniId>().eq(UniId::getUniId, uniId.getUniId()));
        if (one == null) {
            boolean saved = uniIdService.save(uniId);
            return ResponseProcess.returnString(saved,"添加成功","添加失败");
        }
        return ResponseResult.fail(null,"不要重复添加");
    }
}
