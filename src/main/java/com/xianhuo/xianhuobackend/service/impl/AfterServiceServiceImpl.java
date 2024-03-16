package com.xianhuo.xianhuobackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xianhuo.xianhuobackend.mapper.AfterServiceMapper;
import com.xianhuo.xianhuobackend.entity.AfterService;
import com.xianhuo.xianhuobackend.service.AfterServiceService;
import org.springframework.stereotype.Service;

/**
 * 售后表(AfterService)表服务实现类
 *
 * @author makejava
 * @since 2024-03-14 20:58:44
 */
@Service("afterServiceService")
public class AfterServiceServiceImpl extends ServiceImpl<AfterServiceMapper, AfterService> implements AfterServiceService {

}

