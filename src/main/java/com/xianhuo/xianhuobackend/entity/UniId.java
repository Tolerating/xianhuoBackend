package com.xianhuo.xianhuobackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;

/**
 * (UniId)表实体类
 *
 * @author makejava
 * @since 2024-02-14 19:34:50
 */
@Data
@TableName("uni_id")
@SuppressWarnings("serial")
public class UniId {
//id
    @TableId(type = IdType.AUTO)
    private Long id;
//闲货系统用户id
    private Long xhId;
//uni统一登录的用户id
    private String uniId;
//uni统一登录的token
    private String uniToken;
}

