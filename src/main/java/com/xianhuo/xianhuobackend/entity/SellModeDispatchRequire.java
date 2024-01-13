package com.xianhuo.xianhuobackend.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * (SellModeDispatchRequire)表实体类
 *
 * @author makejava
 * @since 2024-01-13 14:45:08
 */
@Data
@TableName("sell_mode_dispatch_require")
@SuppressWarnings("serial")
public class SellModeDispatchRequire {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long sellModeId;

    private Long dispatchId;

    private Long productRequireId;
    //状态
    private Integer status;
    //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    //更新时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;


}

