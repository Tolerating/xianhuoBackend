package com.xianhuo.xianhuobackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xianhuo.xianhuobackend.entity.SellModeDispatchRequire;

import java.util.List;

public interface SellModeDIspatchRequireService extends IService<SellModeDispatchRequire> {
    List<SellModeDispatchRequire> allMode();
}