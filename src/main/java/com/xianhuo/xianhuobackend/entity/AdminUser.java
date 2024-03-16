package com.xianhuo.xianhuobackend.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * (AdminUser)表实体类
 *
 * @author makejava
 * @since 2024-03-10 14:45:56
 */
@Data
@TableName("admin_user")
@SuppressWarnings("serial")
public class AdminUser {
    //管理员id
    @TableId(type = IdType.AUTO)
    private Long id;
    //管理员账号
    private String account;
    //管理员密码
    private String password;
    //1表示超级管理员，0表示校园管理员
    private Integer authority;
    //学校定位，用于校园管理员
    private String school;
    //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}

