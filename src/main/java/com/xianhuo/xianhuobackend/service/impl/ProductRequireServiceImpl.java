package com.xianhuo.xianhuobackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xianhuo.xianhuobackend.entity.ProductRequire;
import com.xianhuo.xianhuobackend.entity.SellModeDispatchRequire;
import com.xianhuo.xianhuobackend.mapper.ProductRequireMapper;
import com.xianhuo.xianhuobackend.mapper.SellModeDispatchRequireMapper;
import com.xianhuo.xianhuobackend.service.ProductRequireService;
import com.xianhuo.xianhuobackend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductRequireServiceImpl extends ServiceImpl<ProductRequireMapper, ProductRequire> implements ProductRequireService {
    @Autowired
    private SellModeDispatchRequireMapper sellModeDispatchRequireMapper;
    @Autowired
    private ProductRequireMapper productRequireMapper;

    @Override
    public List<ProductRequire> requireBySellAndDispatch(long sellId, long dispatchId) {
        LambdaQueryWrapper<SellModeDispatchRequire> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SellModeDispatchRequire::getSellModeId,sellId)
                .eq(SellModeDispatchRequire::getDispatchId,dispatchId);
        List<Long> sellModeDispatchRequires = sellModeDispatchRequireMapper.selectList(wrapper)
                .stream()
                .map(SellModeDispatchRequire::getProductRequireId)
                .collect(Collectors.toList());
        return productRequireMapper.selectBatchIds(sellModeDispatchRequires);

    }
}
