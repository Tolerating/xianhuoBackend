package com.xianhuo.xianhuobackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * (ChatList)聊天列表
 *
 * @author makejava
 * @since 2024-02-16 22:07:23
 */
@Data
@TableName("chat_list")
@SuppressWarnings("serial")
public class ChatList {
    //聊天列表主键
    @TableId(type = IdType.AUTO)
    private Long listId;
    //聊天主表id
    private Long linkId;
    //发送者id
    private Long fromUser;
    //接收者id
    private Long toUser;
    //    发送者昵称
    @TableField(exist = false)
    private String fromUserName;
    //    接受者昵称
    @TableField(exist = false)
    private String toUserName;
    //    接收者头像
    @TableField(exist = false)
    private String toUserPicture;
    //    发送者头像
    @TableField(exist = false)
    private String fromUserPicture;
    //发送方是否在窗口
    private Integer fromWindow;
    //接收方是否在窗口
    private Integer toWindow;
    //未读数
    private Integer unread;
    //是否删除,0未删除，1删除
    private Integer status;
    @TableField(exist = false)
    private String content;
    @TableField(exist = false)
    private String type;
    //发送时间
    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date sendTime;


}

