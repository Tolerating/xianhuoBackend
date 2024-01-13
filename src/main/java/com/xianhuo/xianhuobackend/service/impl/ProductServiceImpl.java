package com.xianhuo.xianhuobackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xianhuo.xianhuobackend.entity.Category;
import com.xianhuo.xianhuobackend.entity.Product;
import com.xianhuo.xianhuobackend.entity.SellMode;
import com.xianhuo.xianhuobackend.mapper.CategoryMapper;
import com.xianhuo.xianhuobackend.mapper.ProductMapper;
import com.xianhuo.xianhuobackend.mapper.ProductRequireMapper;
import com.xianhuo.xianhuobackend.mapper.SellModeMapper;
import com.xianhuo.xianhuobackend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {
    @Autowired
    private CategoryMapper xhCategoryMapper;
    @Autowired
    private SellModeMapper sellModeMapper;

    @Override
    public List<Category> allCategory() {
        return xhCategoryMapper.selectList(new QueryWrapper<Category>());
    }

    @Override
    public List<SellMode> allUsableSellMode() {
        LambdaQueryWrapper<SellMode> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SellMode::getStatus,1);
        return sellModeMapper.selectList(wrapper);
    }




}
