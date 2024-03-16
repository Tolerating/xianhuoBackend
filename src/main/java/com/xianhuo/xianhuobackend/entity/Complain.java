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
 * 投诉表(Complain)表实体类
 *
 * @author makejava
 * @since 2024-03-12 21:28:14
 */
@Data
@TableName("complain")
@SuppressWarnings("serial")
public class Complain {
    //投诉表主键
    @TableId(type = IdType.AUTO)
    private Long id;
    //投诉人id
    private Long complainantId;
    //投诉原因
    private String complainantCause;
    //被投诉的商品或帖子
    private Long complainantSubject;
    //被投诉人id
    private Long sellerId;
    //投诉类别，1表示商品，0表示求购帖子
    private Integer type;
    //处理人id
    private Long dealUser;
    //处理方式描述
    private String dealMethod;
    //投诉时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    //处理时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dealTime;

}

