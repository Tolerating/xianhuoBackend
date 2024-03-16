package com.xianhuo.xianhuobackend.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * (ChatMessage)聊天内容表
 *
 * @author makejava
 * @since 2024-02-16 22:07:23
 */
@Data
@TableName("chat_message")
@SuppressWarnings("serial")
public class ChatMessage {
    //聊天内容id
    @TableId(type = IdType.AUTO)
    private Long messageId;
    //聊天主表id
    private Long linkId;
    //发送者
    private Long fromUser;
    //接收者
    private Long toUser;
//    发送者名字
    @TableField(exist = false)
    private String senderName;
//    发送者头像
    @TableField(exist = false)
    private String senderAvatar;
//    消息类型，用户前端通知展示
    @TableField(exist = false)
    private String showType;
    //聊天内容
    private String content;
    //发送时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date sendTime;
    //消息类型
    private Integer type;
    //是否为最后一条信息
    private Integer isLatest;

}

