package com.xianhuo.xianhuobackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.xianhuo.xianhuobackend.common.ResponseProcess;
import com.xianhuo.xianhuobackend.common.ResponseResult;
import com.xianhuo.xianhuobackend.entity.Favourite;
import com.xianhuo.xianhuobackend.entity.Product;
import com.xianhuo.xianhuobackend.service.FavouriteService;
import com.xianhuo.xianhuobackend.utils.JWTUtil;
import jdk.nashorn.internal.objects.annotations.Getter;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class FavouriteController {
    @Autowired
    private FavouriteService favouriteService;
    @Autowired
    HttpServletRequest httpServletRequest;
//    获取收藏数量
    @GetMapping("/favourite/count")
    public ResponseResult favouriteCount(){
        String authorization = httpServletRequest.getHeader("authorization");
        String id = JWTUtil.parseJWT(authorization).getId();
        long count = favouriteService.count(new LambdaQueryWrapper<Favourite>()
                .eq(Favourite::getUserId, id)
                .eq(Favourite::getStatus,0));
        return ResponseResult.ok(count,"success");
    }

//    查询商品是否已经被收藏
    @GetMapping("/favourite")
    public ResponseResult queryFavourite(Long userId,Long productId){
        Favourite one = favouriteService.getOne(new LambdaQueryWrapper<Favourite>()
                .eq(Favourite::getUserId, userId)
                .eq(Favourite::getProductId, productId)
                .eq(Favourite::getStatus,0));
        if(one != null){
            return ResponseResult.ok(1,"success");
        }else{
            return ResponseResult.ok(0,"success");
        }
    }
//    添加新的收藏
    @PostMapping("/favourite")
    public ResponseResult addFavourite(@RequestBody Favourite favourite){
        Favourite one = favouriteService.getOne(new LambdaQueryWrapper<Favourite>()
                .eq(Favourite::getUserId, favourite.getUserId())
                .eq(Favourite::getProductId, favourite.getProductId()));
        if(one == null){
            boolean saved = favouriteService.save(favourite);
            return ResponseProcess.returnString(saved,"收藏成功","收藏失败");
        }else{
            boolean update = favouriteService.update(new LambdaUpdateWrapper<Favourite>()
                    .set(Favourite::getStatus, 0)
                    .eq(Favourite::getUserId, favourite.getUserId())
                    .eq(Favourite::getProductId, favourite.getProductId()));
            return ResponseProcess.returnString(update,"收藏成功","收藏失败");
        }
    }
//    取消收藏
    @DeleteMapping("/favourite")
    public ResponseResult cancelFavourite(Long userId,Long productId){
        boolean update = favouriteService.update(new LambdaUpdateWrapper<Favourite>()
                .set(Favourite::getStatus, 1)
                .eq(Favourite::getUserId, userId)
                .eq(Favourite::getProductId, productId));
        return ResponseProcess.returnString(update,"取消收藏成功","取消收藏失败");
    }
//  根据userId获取所有的收藏
    @GetMapping("/favourite/all/{userId}")
    public ResponseResult allFavouriteByUserId(@PathVariable("userId")Long id){
        List<Product> list = favouriteService.favouriteProductByUserId(id);
        return ResponseProcess.returnList(list);
    }
}
