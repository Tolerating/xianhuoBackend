package com.xianhuo.xianhuobackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xianhuo.xianhuobackend.entity.DispatchMode;
import com.xianhuo.xianhuobackend.entity.SellModeDispatchRequire;
import com.xianhuo.xianhuobackend.mapper.DispatchModeMapper;
import com.xianhuo.xianhuobackend.mapper.SellModeDispatchRequireMapper;
import com.xianhuo.xianhuobackend.service.DispatchModeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DispatchModeServiceImpl extends ServiceImpl<DispatchModeMapper, DispatchMode> implements DispatchModeService {
    @Autowired
    private SellModeDispatchRequireMapper sellModeDispatchRequireMapper;
    @Autowired
    private DispatchModeMapper dispatchModeMapper;
    @Override
    public List<DispatchMode> getUsableDispatchModeBySellId(long sell_id) {
        LambdaQueryWrapper<SellModeDispatchRequire> ruleswrapper = new LambdaQueryWrapper<>();
        ruleswrapper.select(SellModeDispatchRequire::getDispatchId)
                .eq(SellModeDispatchRequire::getSellModeId,sell_id);
        List<Long> requires = sellModeDispatchRequireMapper.selectList(ruleswrapper).stream()
                .distinct()
                .map(SellModeDispatchRequire::getDispatchId)
                .collect(Collectors.toList());
        List<DispatchMode> dispatchModes = dispatchModeMapper.selectBatchIds(requires)
                .stream()
                .filter(e->e.getStatus().equals(1))
                .collect(Collectors.toList());
        return dispatchModes;
    }
}
