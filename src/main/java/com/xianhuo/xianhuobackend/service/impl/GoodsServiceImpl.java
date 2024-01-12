package com.xianhuo.xianhuobackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xianhuo.xianhuobackend.entity.XhCategory;
import com.xianhuo.xianhuobackend.mapper.XhCategoryMapper;
import com.xianhuo.xianhuobackend.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GoodsServiceImpl implements GoodsService {
    @Autowired
    private XhCategoryMapper xhCategoryMapper;
    @Override
    public List<XhCategory> allCategory() {
        return xhCategoryMapper.selectList(new QueryWrapper<XhCategory>());
    }
}
