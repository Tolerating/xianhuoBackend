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
 * 售后表(AfterService)表实体类
 *
 * @author makejava
 * @since 2024-03-14 20:58:44
 */
@Data
@TableName("after_service")
@SuppressWarnings("serial")
public class AfterService {
    //主键
    @TableId(type = IdType.AUTO)
    private Long id;
    //买家id
    @TableField(value = "buyer_id")
    private Long buyerId;
    //退货原因
    private String cause;
//    商品描述
    private String productDetail;
//    商品金额
    private Double productPrice;
    //图片,逗号分隔
    private String images;
    //商品id
    private Long productId;
    //订单表id
    private Long orderId;
    //卖家id
    private Long sellerId;
    //商家处理状态，0为等待商家处理，1为商家同意，2表示商家不同意，
    private Integer sellerStatus;
    //商家拒绝原因
    private String sellerRefuseCause;
    //商家处理时间
    private Date sellerDealTime;
    //平台处理状态，-1表示平台未介入，0表示平台介入，1表示处理完成
    private Integer platformStatus;
//    平台处理结果
    private String platformResult;
//    平台处理人id
    private Long platformUser;
    // 售后表状态，0表示售后处理中，-1表示售后失败，1表示售后成功
    private Integer status;
    //平台处理时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date platformDealTime;
    //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;


}

