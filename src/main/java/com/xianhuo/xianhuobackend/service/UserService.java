package com.xianhuo.xianhuobackend.service;

import com.xianhuo.xianhuobackend.entity.Users;

public interface UserService {
    Users loginPhone(String phone, String pwd);
}
