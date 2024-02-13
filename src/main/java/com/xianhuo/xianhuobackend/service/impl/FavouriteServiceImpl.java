package com.xianhuo.xianhuobackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xianhuo.xianhuobackend.entity.Favourite;
import com.xianhuo.xianhuobackend.mapper.FavouriteMapper;
import com.xianhuo.xianhuobackend.service.FavouriteService;
import org.springframework.stereotype.Service;

@Service
public class FavouriteServiceImpl extends ServiceImpl<FavouriteMapper, Favourite> implements FavouriteService {
}
