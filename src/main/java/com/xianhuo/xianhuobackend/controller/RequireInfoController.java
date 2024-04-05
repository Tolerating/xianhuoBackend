package com.xianhuo.xianhuobackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xianhuo.xianhuobackend.common.ResponseProcess;
import com.xianhuo.xianhuobackend.common.ResponseResult;
import com.xianhuo.xianhuobackend.entity.RequireInfo;
import com.xianhuo.xianhuobackend.service.RequireInfoService;
import com.xianhuo.xianhuobackend.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.Response;
import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class RequireInfoController {
    @Autowired
    private RequireInfoService requireInfoService;
    @Autowired
    HttpServletRequest httpServletRequest;
    @PostMapping("/requireInfo")
    public ResponseResult addInfo(@RequestBody RequireInfo requireInfo){
        long rId = new Date().getTime();
        requireInfo.setId(rId);
        boolean saved = requireInfoService.save(requireInfo);
        if(saved){
            return ResponseResult.ok(rId,"发布成功");
        }
        return ResponseResult.fail(null,"发布失败");
    }

//    分页获取需求
    @GetMapping("/requireInfo")
    public ResponseResult pageInfo(Long categoryId,Integer current,Integer size){
        Page<RequireInfo> page = new Page<>(current, size);
        Page<RequireInfo> requireInfoPage = requireInfoService.page(page, new LambdaQueryWrapper<RequireInfo>()
                .eq(categoryId != 0, RequireInfo::getCategoryId, categoryId)
                .eq(RequireInfo::getStatus,1));
        return ResponseProcess.returnObject(requireInfoPage);
    }
//    根据id获取需求信息
    @GetMapping("/requireInfo/{id}")
    public ResponseResult getInfoById(@PathVariable("id") Long id){
        RequireInfo id1 = requireInfoService.getById(id);
        return ResponseProcess.returnObject(id1);
    }
// 获取用户发布的需求数量
    @GetMapping("/requireInfo/count")
    public ResponseResult getInfoCount(){
        String authorization = httpServletRequest.getHeader("authorization");
        String id = JWTUtil.parseJWT(authorization).getId();
        long count = requireInfoService.count(new LambdaQueryWrapper<RequireInfo>()
                .eq(RequireInfo::getUserId, id)
                .eq(RequireInfo::getStatus,1));
        return ResponseResult.ok(count,"success");

    }

//    获取用户发布的求购
    @GetMapping("/requireInfo/user")
    public ResponseResult getUserInfo(){
        String authorization = httpServletRequest.getHeader("authorization");
        String id = JWTUtil.parseJWT(authorization).getId();
        List<RequireInfo> list = requireInfoService.list(new LambdaQueryWrapper<RequireInfo>()
                .eq(RequireInfo::getUserId, id)
                .eq(RequireInfo::getStatus,1));
        return ResponseProcess.returnList(list);

    }

    @GetMapping("/requireInfo/cancle/{id}")
    public ResponseResult cancelInfo(@PathVariable("id")Long id){
        boolean update = requireInfoService.update(new LambdaUpdateWrapper<RequireInfo>()
                .set(RequireInfo::getStatus, -1)
                .eq(RequireInfo::getId, id));
        return ResponseProcess.returnString(update,"更新成功","更新失败");

    }
}
