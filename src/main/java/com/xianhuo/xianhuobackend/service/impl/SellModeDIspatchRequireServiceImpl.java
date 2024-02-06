package com.xianhuo.xianhuobackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xianhuo.xianhuobackend.entity.SellModeDispatchRequire;
import com.xianhuo.xianhuobackend.mapper.SellModeDispatchRequireMapper;
import com.xianhuo.xianhuobackend.service.SellModeDIspatchRequireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SellModeDIspatchRequireServiceImpl extends ServiceImpl<SellModeDispatchRequireMapper, SellModeDispatchRequire> implements SellModeDIspatchRequireService {
    @Autowired
    private SellModeDispatchRequireMapper sellModeDispatchRequireMapper;
    @Override
    public List<SellModeDispatchRequire> allMode() {
        return sellModeDispatchRequireMapper.modes();
    }
}
