package com.xianhuo.xianhuobackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xianhuo.xianhuobackend.mapper.OrderInfoMapper;
import com.xianhuo.xianhuobackend.entity.OrderInfo;
import com.xianhuo.xianhuobackend.service.OrderInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (OrderInfo)表服务实现类
 *
 * @author makejava
 * @since 2024-03-01 22:20:12
 */
@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements OrderInfoService {
    @Resource
    private OrderInfoMapper orderInfoMapper;

    @Override
    public List<OrderInfo> getBuyerHistory(Long buyerId) {
        return orderInfoMapper.getBuyerHistory(buyerId);
    }

    @Override
    public List<OrderInfo> getSellerHistory(Long sellerId) {
        return orderInfoMapper.getSellerHistory(sellerId);
    }
}

