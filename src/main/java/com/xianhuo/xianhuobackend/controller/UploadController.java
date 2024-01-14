package com.xianhuo.xianhuobackend.controller;

import com.xianhuo.xianhuobackend.common.ResponseResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class UploadController {

    @PostMapping("/upload")
    public ResponseResult uploadFile(@RequestParam("file")MultipartFile file){

        return null;
    }
}
