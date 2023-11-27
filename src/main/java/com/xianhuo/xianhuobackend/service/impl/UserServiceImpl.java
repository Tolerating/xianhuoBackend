package com.xianhuo.xianhuobackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xianhuo.xianhuobackend.entity.Users;
import com.xianhuo.xianhuobackend.mapper.UsersMapper;
import com.xianhuo.xianhuobackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UsersMapper usersMapper;
    @Override
    public Users loginPhone(String phone, String pwd) {

        LambdaQueryWrapper<Users> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Users::getPhone,phone)
                .eq(Users::getPassword,pwd);

        Users user = usersMapper.selectOne(wrapper);
        return user;
    }
}
