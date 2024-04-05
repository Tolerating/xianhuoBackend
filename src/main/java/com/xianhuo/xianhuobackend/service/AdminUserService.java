package com.xianhuo.xianhuobackend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xianhuo.xianhuobackend.entity.AdminUser;
import com.xianhuo.xianhuobackend.entity.AfterService;
import com.xianhuo.xianhuobackend.entity.Complain;
import com.xianhuo.xianhuobackend.entity.Product;

/**
 * (AdminUser)表服务接口
 *
 * @author makejava
 * @since 2024-03-10 14:45:56
 */
public interface AdminUserService extends IService<AdminUser> {
    IPage<Complain> getComplainWaitForPost(Page<Complain> page,String startTime,String endTime, Boolean history);
    IPage<Complain> getComplainWaitForProduct(Page<Complain> page,String startTime,String endTime, Boolean history);
    IPage<AfterService> afterServiceWaitDealList(Page<AfterService> page,String startTime,String endTime,Boolean history);

    IPage<Product> getAllProducts(Page<Product> page,String startTime,String endTime,String deatil);
}

