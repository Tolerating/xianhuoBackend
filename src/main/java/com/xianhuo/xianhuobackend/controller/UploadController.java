package com.xianhuo.xianhuobackend.controller;

import com.xianhuo.xianhuobackend.common.ResponseResult;
import com.xianhuo.xianhuobackend.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class UploadController {
    @Autowired
    private HttpServletRequest httpServletRequest;
    @Value("${file-dir}")
    String fileDir;

    //上传单个文件
    @PostMapping("/upload")
    public ResponseResult uploadFile(@RequestPart("file") MultipartFile file) throws IOException {
        String authorization = httpServletRequest.getHeader("authorization");
        String id = JWTUtil.parseJWT(authorization).getId();
        String url = "/img/" + id;
        File mainF = new File(fileDir + url);
        if (!mainF.exists()) {
            mainF.mkdirs();
        }

        if (!file.isEmpty()) {

            String path = String.format("%s/%s/%s", fileDir, url, file.getOriginalFilename());
//            if (!new File(path).exists()) {
//            }
            file.transferTo(new File(path));

            return ResponseResult.ok(url+"/"+file.getOriginalFilename(), "success");

        }

        return ResponseResult.ok("图片不能为空", "error");
    }
}
