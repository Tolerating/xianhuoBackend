package com.xianhuo.xianhuobackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.xianhuo.xianhuobackend.common.ResponseProcess;
import com.xianhuo.xianhuobackend.common.ResponseResult;
import com.xianhuo.xianhuobackend.entity.UniIdRequestBody;
import com.xianhuo.xianhuobackend.entity.Users;
import com.xianhuo.xianhuobackend.service.UniIdService;
import com.xianhuo.xianhuobackend.service.UserService;
import com.xianhuo.xianhuobackend.utils.HMACSha256;
import com.xianhuo.xianhuobackend.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

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
    @Autowired
    private UniIdService uniIdService;
//    邮箱登录
    @PostMapping("/login")
    public ResponseResult<Object> login(@RequestBody Users user){
        System.out.println(user);
        Users users = userService.loginEmail(user);
        if(users != null){
            String token = JWTUtil.createJWT(users.getId().toString(), "", "");
            System.out.println("token："+token);
            return ResponseResult.ok(String.format("%s,%s", users.getName(),token),"登录成功");
        }else{

            return ResponseResult.fail(users,"用户名或密码错误！");
        }
    }
    //    完善用户信息
    @PostMapping("/improveInfo")
    public ResponseResult improveInfo(@RequestBody Users users){
        String authorization = httpServletRequest.getHeader("authorization");
        String id = JWTUtil.parseJWT(authorization).getId();
        LambdaUpdateWrapper<Users> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(Users::getName,users.getName())
                .set(Users::getAvatar,users.getAvatar())
                .set(Users::getSchool,users.getSchool())
                .set(Users::getLocation,users.getLocation())
                .eq(Users::getId,id);
        boolean update = userService.update(wrapper);
        //注册uni-id
//        String nonce = "sdfafdafsagjhhkbd";
//        long timestamp = System.currentTimeMillis();
//        Map<String, String> params = new HashMap<>();
//        params.put("externalUid",id);
//        params.put("nickname",users.getName());
//        params.put("avatar",users.getAvatar());
//        String signature = HMACSha256.getSignature(params, nonce, timestamp);
//        UniIdRequestBody uniIdRegisterParams = new UniIdRequestBody();
//        uniIdRegisterParams.setExternalUid(id);
//        uniIdRegisterParams.setNickname(users.getName());
//        uniIdRegisterParams.setAvatar(users.getAvatar());
//        WebClient webClient = WebClient.create();
//        webClient.post().uri("https://fc-mp-cb2eb9b5-3cbb-47a7-aa08-383cdf5374d2.next.bspapp.com/externalRegister")
//                .header("uni-id-nonce",nonce)
//                .header("uni-id-timestamp", String.valueOf(timestamp))
//                .header("uni-id-signature",signature)
//                .body(Mono.just(uniIdRegisterParams), UniIdRequestBody.class);
        return ResponseProcess.returnString(update,"成功","更新失败");
    }

    @GetMapping("/user/session")
    public ResponseResult setSession(HttpSession httpSession){
        String authorization = httpServletRequest.getHeader("authorization");
        String id = JWTUtil.parseJWT(authorization).getId();
        Users users = userService.getById(id);
        httpSession.setAttribute("user",users);
        return ResponseResult.ok(null,"session设置成功");

    }

    @PostMapping("/uniId/registerSignature")
    public ResponseResult getSignature(@RequestBody Users users){
        String authorization = httpServletRequest.getHeader("authorization");
        String id = JWTUtil.parseJWT(authorization).getId();
        String nonce = "sdfafdafsagjhhkbd";
        long timestamp = System.currentTimeMillis();
        Map<String, String> params = new HashMap<>();
        params.put("externalUid",id);
        params.put("nickname",users.getName());
        params.put("avatar",users.getAvatar());
        String signature = HMACSha256.getSignature(params, nonce, timestamp);
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("nonce",nonce);
        hashMap.put("timestamp", String.valueOf(timestamp));
        hashMap.put("signature",signature);
        return ResponseProcess.returnObject(hashMap);
    }


    @GetMapping("/uniId/loginSignature")
    public ResponseResult getLoginSignature(){
        String authorization = httpServletRequest.getHeader("authorization");
        String id = JWTUtil.parseJWT(authorization).getId();
        System.out.println(id);
        String nonce = "sdfafdafsagjhhkbd";
        long timestamp = System.currentTimeMillis();
        Map<String, String> params = new HashMap<>();
        params.put("externalUid",id);
        String signature = HMACSha256.getSignature(params, nonce, timestamp);
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("nonce",nonce);
        hashMap.put("timestamp", String.valueOf(timestamp));
        hashMap.put("signature",signature);
        return ResponseProcess.returnObject(hashMap);
    }


//    根据userId获取部分信息
    @GetMapping("/user/{userId}")
    public ResponseResult<Users> userById(@PathVariable("userId") Long userId){
        LambdaQueryWrapper<Users> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Users::getAvatar,Users::getName,Users::getSchool,Users::getId)
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
    // 根据邮箱更新密码
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

//    根据token获取用户信息
    @GetMapping("/user")
    public ResponseResult<Users> user(){
        String authorization = httpServletRequest.getHeader("authorization");
        String id = JWTUtil.parseJWT(authorization).getId();
        Users users = userService.getById(id);
        return ResponseProcess.returnObject(users);

    }
    //更新用户是否在聊天页
    @GetMapping("/inChat")
    public ResponseResult updateInChat(Integer status){
        String authorization = httpServletRequest.getHeader("authorization");
        String id = JWTUtil.parseJWT(authorization).getId();
        boolean update = userService.update(new LambdaUpdateWrapper<Users>()
                .set(Users::getInChat, status)
                .eq(Users::getId, id));
        return ResponseProcess.returnString(update,"sueccess","fail");

    }
//    发送邮箱验证码
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
