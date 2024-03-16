package com.xianhuo.xianhuobackend.controller;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xianhuo.xianhuobackend.common.ResponseProcess;
import com.xianhuo.xianhuobackend.common.ResponseResult;
import com.xianhuo.xianhuobackend.entity.AdminUser;
import com.xianhuo.xianhuobackend.service.AdminUserService;
import com.xianhuo.xianhuobackend.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class AdminController {
    @Resource
    private AdminUserService adminUserService;
    @Autowired
    HttpServletRequest httpServletRequest;
//    登录
    @PostMapping("/admin/login")
    public ResponseResult adminLogin(@RequestBody AdminUser adminUser){
        AdminUser user = adminUserService.getOne(new LambdaQueryWrapper<AdminUser>()
                .eq(AdminUser::getAccount, adminUser.getAccount())
                .eq(AdminUser::getPassword, adminUser.getPassword()));
        if(user != null){
            String token = JWTUtil.createJWT(user.getId().toString(), "", "");
            System.out.println("token："+token);
            return ResponseResult.ok(token,"登录成功");
        }else{

            return ResponseResult.fail(user,"用户名或密码错误！");
        }
    }

//    获取管理员信息
    @GetMapping("/admin/info")
    public ResponseResult adminInfo(){
        String authorization = httpServletRequest.getHeader("authorization");
        String id = JWTUtil.parseJWT(authorization).getId();
        AdminUser adminUser = adminUserService.getById(id);
        return ResponseProcess.returnObject(adminUser);
    }

//    分页获取
}
