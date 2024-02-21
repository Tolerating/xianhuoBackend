package com.xianhuo.xianhuobackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xianhuo.xianhuobackend.entity.RequireInfo;
import com.xianhuo.xianhuobackend.mapper.RequireInfoMapper;
import com.xianhuo.xianhuobackend.service.RequireInfoService;
import org.springframework.stereotype.Service;

@Service
public class RequireInfoServiceImpl extends ServiceImpl<RequireInfoMapper, RequireInfo> implements RequireInfoService {
}
