package com.xianhuo.xianhuobackend.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * 官方通知(Notice)表实体类
 *
 * @author makejava
 * @since 2024-03-16 16:56:26
 */
@Data
@NoArgsConstructor
@TableName("notice")
@SuppressWarnings("serial")
public class Notice {
    //主键
    @TableId(type = IdType.AUTO)
    private Long id;
    //通知标题
    private String title;
    //通知内容
    private String content;
    //通知接收者id
    private Long receiverId;
    //类型，0表示帖子，1表示商品
    private Integer attachType;
    //商品id或帖子id
    private Long attach;
//    发布人id
    private Long publisher;
    //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    public Notice(String title, String content, Long receiverId, Integer attachType, Long attach, Long publisher) {
        this.title = title;
        this.content = content;
        this.receiverId = receiverId;
        this.attachType = attachType;
        this.attach = attach;
        this.publisher = publisher;
    }
}

