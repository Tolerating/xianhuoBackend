package com.xianhuo.xianhuobackend.controller;

import com.xianhuo.xianhuobackend.common.ResponseResult;
import com.xianhuo.xianhuobackend.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class UploadController {
    @Autowired
    private HttpServletRequest httpServletRequest;

    //上传单个文件
    @PostMapping("/upload")
    public ResponseResult uploadFile(@RequestPart("file") MultipartFile file) throws IOException {
        String authorization = httpServletRequest.getHeader("authorization");
        String id = JWTUtil.parseJWT(authorization).getId();
        File mainF = new File(System.getProperty("user.dir")+"\\src\\main\\resources\\static\\productImg\\"+id);
        if (!mainF.exists()) {
            mainF.mkdirs();
        }

        if(!file.isEmpty()){
            System.out.println(file.getOriginalFilename());
            String path = System.getProperty("user.dir")+"\\src\\main\\resources\\static\\productImg\\"+id+"\\"+file.getOriginalFilename();
            if (!new File(path).exists()) {
                file.transferTo(new File(path));
            }
            String url = httpServletRequest.getScheme() + "://"+ httpServletRequest.getServerName() +
                    ":" + httpServletRequest.getServerPort() + httpServletRequest.getContextPath() + "/img/"+id+"/"+file.getOriginalFilename();
            return ResponseResult.ok(url,"success");

        }

        return ResponseResult.ok("图片不能为空","error");
    }
}
