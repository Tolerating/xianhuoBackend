package com.xianhuo.xianhuobackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.xianhuo.xianhuobackend.common.ResponseProcess;
import com.xianhuo.xianhuobackend.common.ResponseResult;
import com.xianhuo.xianhuobackend.entity.Users;
import com.xianhuo.xianhuobackend.service.UserService;
import com.xianhuo.xianhuobackend.utils.JWTUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.Response;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    HttpServletRequest httpServletRequest;
    @Value("${spring.mail.username}")
    private String from;
    @Autowired
    private JavaMailSender mailSender;
    @PostMapping("/login")
    public ResponseResult<Object> login(@RequestBody Users user){
        System.out.println(user);
        Users users = userService.loginEmail(user);
        if(users != null){
            String toekn = JWTUtil.createJWT(users.getId().toString(), "", "");
            return ResponseResult.ok(String.format("%s,%s", users.getName(),toekn),"登录成功");
        }else{

            return ResponseResult.fail(users,"用户名或密码错误！");
        }
    }

//    根据userId获取部分信息
    @GetMapping("/user/{userId}")
    public ResponseResult<Users> userById(@PathVariable("userId") Long userId){
        LambdaQueryWrapper<Users> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Users::getAvatar,Users::getName,Users::getSchool)
                .eq(Users::getId,userId);
        Users users = userService.getOne(wrapper);
        return ResponseProcess.returnObject(users);
    }
//    注册用户
    @PostMapping("/register")
    public ResponseResult registerByEmail(@RequestBody Users user) {
        Users one = userService.getOne(new LambdaQueryWrapper<Users>().eq(Users::getEmail, user.getEmail()));
        if (one!=null){
            return ResponseResult.fail(null,"该账户已被注册");
        }
        boolean saved = userService.save(user);
        return ResponseProcess.returnString(saved,"注册成功","注册失败");
    }

    @PostMapping("/updatePassword")
    public ResponseResult updateByEmail(@RequestBody Users user){
        Users one = userService.getOne(new LambdaQueryWrapper<Users>().eq(Users::getEmail, user.getEmail()));
        if (one==null){
            return ResponseResult.fail(null,"该账户不存在");
        }
        one.setPassword(user.getPassword());
        boolean update = userService.updateById(one);
        return ResponseProcess.returnString(update,"更新成功","更新失败");
    }

    @GetMapping("/user")
    public ResponseResult<Users> user(){
        String authorization = httpServletRequest.getHeader("authorization");
        String id = JWTUtil.parseJWT(authorization).getId();
        Users users = userService.getById(id);
        return ResponseProcess.returnObject(users);

    }
    @GetMapping("/mailCode")
    public ResponseResult getCheckCode(String email){
        String checkCode = String.valueOf(new Random().nextInt(899999) + 100000);
        String msg = "您的注册验证码为："+checkCode;
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(email);
            message.setSubject("注册验证码");
            message.setText(msg);
            mailSender.send(message);

        }catch (Exception e){
            return null;
        }
        return ResponseResult.ok(checkCode,"mail");


    }
}
