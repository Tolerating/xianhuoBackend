package com.xianhuo.xianhuobackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xianhuo.xianhuobackend.entity.OrderInfo;

import java.util.List;

/**
 * (OrderInfo)表服务接口
 *
 * @author makejava
 * @since 2024-03-01 22:20:12
 */
public interface OrderInfoService extends IService<OrderInfo> {
    List<OrderInfo> getBuyerHistory(Long buyerId);
    List<OrderInfo> getSellerHistory(Long sellerId);
}

