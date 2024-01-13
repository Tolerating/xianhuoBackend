package com.xianhuo.xianhuobackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xianhuo.xianhuobackend.entity.Category;
import com.xianhuo.xianhuobackend.mapper.CategoryMapper;
import com.xianhuo.xianhuobackend.service.CategoryService;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
}
