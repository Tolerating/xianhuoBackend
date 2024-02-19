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
 * (Favourite)表实体类
 *
 * @author makejava
 * @since 2024-02-13 22:48:09
 */
@Data
@TableName("favourite")
@SuppressWarnings("serial")
public class Favourite {
    //收藏id
    @TableId(type = IdType.AUTO)
    private Long id;
    //用户id
    private Long userId;
    //商品id
    private Long productId;
    //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    //状态，0表示未删除，1表示删除
    private Integer status;

}

