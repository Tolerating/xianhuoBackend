package com.xianhuo.xianhuobackend.controller;



import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xianhuo.xianhuobackend.common.ResponseProcess;
import com.xianhuo.xianhuobackend.common.ResponseResult;
import com.xianhuo.xianhuobackend.entity.AfterService;
import com.xianhuo.xianhuobackend.service.AfterServiceService;
import com.xianhuo.xianhuobackend.utils.JWTUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;

/**
 * 售后表(AfterService)表控制层
 *
 * @author makejava
 * @since 2024-03-14 20:58:43
 */
@RestController
@CrossOrigin
@RequestMapping("/api")
public class AfterServiceController {
    /**
     * 服务对象
     */
    @Resource
    private AfterServiceService afterServiceService;

    @Resource
    private HttpServletRequest httpServletRequest;

//    申请售后
    @PostMapping("/afterService")
    public ResponseResult addAfter(@RequestBody AfterService afterService){
        boolean saved = afterServiceService.save(afterService);
        return ResponseProcess.returnString(saved,"申请成功","申请失败");
    }

//    根据购买用户id和订单号获取售后记录
    @GetMapping("/afterService")
    public ResponseResult getAfter(Long buyerId,Long orderId){
        AfterService one = afterServiceService.getOne(new LambdaQueryWrapper<AfterService>()
                .eq(AfterService::getBuyerId, buyerId)
                .eq(AfterService::getOrderId, orderId));
        return ResponseProcess.returnObject(one);
    }

//    获取购买者的所有售后
    @GetMapping("/afterServices")
    public ResponseResult allAfter(Long buyerId){
        List<AfterService> list = afterServiceService.list(new LambdaQueryWrapper<AfterService>()
                .eq(AfterService::getBuyerId, buyerId)
                .eq(AfterService::getStatus, 0));
        return ResponseProcess.returnList(list);
    }

//    申请平台介入
    @GetMapping("/askPlatform")
    public ResponseResult askPlatform(Long afterId){
        boolean update = afterServiceService.update(new LambdaUpdateWrapper<AfterService>()
                .set(AfterService::getPlatformStatus, 0)
                .eq(AfterService::getId, afterId));
        return ResponseProcess.returnString(update,"申请成功","申请失败");
    }

//    获取售后记录
    @GetMapping("/afterHistory")
    public ResponseResult afterHistory(){
        String authorization = httpServletRequest.getHeader("authorization");
        String id = JWTUtil.parseJWT(authorization).getId();
        List<AfterService> list = afterServiceService.list(new LambdaQueryWrapper<AfterService>()
                .ne(AfterService::getStatus,0)
                .eq(AfterService::getBuyerId, id)
                .or()
                .eq(AfterService::getSellerId, id));
        return ResponseProcess.returnList(list);
    }
}

