package com.xianhuo.xianhuobackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xianhuo.xianhuobackend.entity.Users;

public interface UserService extends IService<Users> {
    Users loginPhone(Users users);
}
