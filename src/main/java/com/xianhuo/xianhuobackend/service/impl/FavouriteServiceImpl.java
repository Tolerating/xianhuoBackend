package com.xianhuo.xianhuobackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xianhuo.xianhuobackend.entity.Favourite;
import com.xianhuo.xianhuobackend.entity.Product;
import com.xianhuo.xianhuobackend.mapper.FavouriteMapper;
import com.xianhuo.xianhuobackend.service.FavouriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavouriteServiceImpl extends ServiceImpl<FavouriteMapper, Favourite> implements FavouriteService {
    @Autowired
    private FavouriteMapper favouriteMapper;
    @Override
    public List<Product> favouriteProductByUserId(Long userId) {
        return favouriteMapper.favouriteProductByUserId(userId);
    }
}
