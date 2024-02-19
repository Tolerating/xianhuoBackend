package com.xianhuo.xianhuobackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xianhuo.xianhuobackend.entity.UniId;
import com.xianhuo.xianhuobackend.mapper.UniIdMapper;
import com.xianhuo.xianhuobackend.service.UniIdService;
import org.springframework.stereotype.Service;

@Service
public class UniIdServiceImpl extends ServiceImpl<UniIdMapper, UniId> implements UniIdService {
}
