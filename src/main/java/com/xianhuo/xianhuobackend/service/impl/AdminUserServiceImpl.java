package com.xianhuo.xianhuobackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xianhuo.xianhuobackend.mapper.AdminUserMapper;
import com.xianhuo.xianhuobackend.entity.AdminUser;
import com.xianhuo.xianhuobackend.service.AdminUserService;
import org.springframework.stereotype.Service;

/**
 * (AdminUser)表服务实现类
 *
 * @author makejava
 * @since 2024-03-10 14:45:56
 */
@Service("adminUserService")
public class AdminUserServiceImpl extends ServiceImpl<AdminUserMapper, AdminUser> implements AdminUserService {

}

