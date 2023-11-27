package com.xianhuo.xianhuobackend.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

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
    private Integer schoolId;
//用户头像
    private String avatar;
//邮箱地址
    private String email;
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
    private Date birthday;
//信誉分
    private Integer score;
//创建时间
    private Date createdAt;
//删除时间
    private Date deletedAt;
}

