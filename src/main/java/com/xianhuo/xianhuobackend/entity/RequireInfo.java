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
 * (RequireInfo)表实体类
 *
 * @author makejava
 * @since 2024-02-21 19:50:49
 */
@Data
@TableName("require_info")
@SuppressWarnings("serial")
public class RequireInfo {
    //需求id
    @TableId(type = IdType.AUTO)
    private Long id;
    //分类id
    private Long categoryId;
    //需求详情
    private String detail;
    //发布者id
    private Long userId;
    //商品状态，1表示需求未解决，-1表示下架
    private Integer status;
    //商品所在学校定位
    private String location;
    //学校名称
    private String school;
    //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

}

