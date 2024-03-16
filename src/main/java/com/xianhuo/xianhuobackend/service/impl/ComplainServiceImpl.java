package com.xianhuo.xianhuobackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xianhuo.xianhuobackend.mapper.ComplainMapper;
import com.xianhuo.xianhuobackend.entity.Complain;
import com.xianhuo.xianhuobackend.service.ComplainService;
import org.springframework.stereotype.Service;

/**
 * 投诉表(Complain)表服务实现类
 *
 * @author makejava
 * @since 2024-03-12 21:28:14
 */
@Service("complainService")
public class ComplainServiceImpl extends ServiceImpl<ComplainMapper, Complain> implements ComplainService {

}

