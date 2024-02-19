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
 * (ChatUserLink)表实体类
 *
 * @author makejava
 * @since 2024-02-16 22:07:23
 */
@Data
@TableName("chat_user_link")
@SuppressWarnings("serial")
public class ChatUserLink {
    //聊天主表id
    @TableId(type = IdType.AUTO)
    private Long linkId;
    //发送方id
    private Long fromUser;
    //接受者id
    private Long toUser;
    //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

}

