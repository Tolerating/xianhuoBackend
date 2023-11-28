package com.xianhuo.xianhuobackend.controller;

import com.xianhuo.xianhuobackend.common.ResponseResult;
import com.xianhuo.xianhuobackend.entity.Users;
import com.xianhuo.xianhuobackend.service.UserService;
import com.xianhuo.xianhuobackend.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Objects;

@RestController
@CrossOrigin
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/login")
    public ResponseResult<Object> login(@RequestBody Users user){
        System.out.println(user);
        Users users = userService.loginPhone(user);
        if(users != null){
            String toekn = JWTUtil.createJWT(users.getId().toString(), "", "");
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("token",toekn);
            hashMap.put("data",users);
            return ResponseResult.ok(hashMap,"登录成功");
        }else{

            return ResponseResult.fail(users,"用户名或密码错误！");
        }
    }
}
