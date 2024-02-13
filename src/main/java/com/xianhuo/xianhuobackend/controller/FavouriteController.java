package com.xianhuo.xianhuobackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xianhuo.xianhuobackend.common.ResponseProcess;
import com.xianhuo.xianhuobackend.common.ResponseResult;
import com.xianhuo.xianhuobackend.entity.Favourite;
import com.xianhuo.xianhuobackend.service.FavouriteService;
import jdk.nashorn.internal.objects.annotations.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class FavouriteController {
    @Autowired
    private FavouriteService favouriteService;

//    添加新的收藏
    @PostMapping("/favourite")
    public ResponseResult addFavourite(@RequestBody Favourite favourite){
        boolean saved = favouriteService.save(favourite);
        return ResponseProcess.returnString(saved,"收藏成功","收藏失败");
    }
//    获取所有的收藏
    @GetMapping("/favourite/{userId}")
    public ResponseResult listFavourite(@PathVariable("userId") Long userId){
        List<Favourite> list = favouriteService.list(new LambdaQueryWrapper<Favourite>().eq(Favourite::getUid, userId));
        return ResponseProcess.returnList(list);
    }
}
