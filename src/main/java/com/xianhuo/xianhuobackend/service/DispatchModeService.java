package com.xianhuo.xianhuobackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xianhuo.xianhuobackend.entity.DispatchMode;
import com.xianhuo.xianhuobackend.entity.SellModeDispatchRequire;

import java.util.List;

public interface DispatchModeService extends IService<DispatchMode> {
    List<DispatchMode> getUsableDispatchModeBySellId(long sell_id);
}
