package com.xianhuo.xianhuobackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xianhuo.xianhuobackend.entity.Favourite;
import com.xianhuo.xianhuobackend.entity.Product;

import java.util.List;

/**
 * (Favourite)表服务接口
 *
 * @author makejava
 * @since 2024-02-13 22:48:09
 */
public interface FavouriteService extends IService<Favourite> {
   List<Product> favouriteProductByUserId(Long userId);
}

