package com.xianhuo.xianhuobackend.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * (Users)表实体类
 *
 * @author makejava
 * @since 2023-11-27 10:45:59
 */
@Data
@TableName("users")
@SuppressWarnings("serial")
public class Users {
    @TableId(type = IdType.AUTO)
    private Long id;
    //用户昵称
    private String name;
    //电话
    private String phone;
    //密码
    private String password;
    //学校id
    private String school;
    //用户头像
    private String avatar;
    //邮箱地址
    private String email;
//    我的收益
    private Double profit;
    //身份证
    private String identityCard;
    //学生学号
    private String stuNumber;
    //班级
    private String grade;
    //学院
    private String faculty;
    //专业
    private String major;

    //生日
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date birthday;
    //信誉分
    private Integer score;
//    是否在聊天页
    private Integer inChat;
    //学校所在定位
    private String location;
    //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;
    //删除时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deletedAt;
}

