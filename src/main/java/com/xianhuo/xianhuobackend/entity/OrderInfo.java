package com.xianhuo.xianhuobackend.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * (OrderInfo)表实体类
 *
 * @author makejava
 * @since 2024-03-01 22:20:12
 */
@Data
@TableName("order_info")
@SuppressWarnings("serial")
public class OrderInfo {
    //订单表id
    @TableId(type = IdType.AUTO)
    private Long id;
    //订单编号
    private String orderId;
    // 售后id
    private Long afterServiceId;
    //能否平台介入标志【商家拒绝过一次即置为1】，0表示不能，1表示行
    private Integer platformFlag;
    // 商家售后处理状态，0为等待商家处理，1为商家同意，2表示商家不同意
    @TableField(exist = false)
    private Integer sellerAfterStatus;
    //平台售后处理状态，-1表示平台未介入，0表示平台介入，1表示处理完成
    @TableField(exist = false)
    private Integer platformAfterStatus;
    // 售后表状态，0表示售后处理中，-1表示售后失败，1表示售后成功，11表示待发货，12表示待收货
    @TableField(exist = false)
    private Integer afterStatus;
    //    售后创建时间
    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date afterCreateTime;
    private String productDetail;
    private String productImages;
    private String productAddress;
    private Long productCategory;
    //支付者id
    private Long buyId;
    //    购买者名字
    @TableField(exist = false)
    private String buyName;
    //购买者id
    private Long sellId;
    //    购买者名字
    @TableField(exist = false)
    private String sellName;
    //    支付宝订单编号
    @TableField(value = "alipay_id")
    private String alipayId;
    //商品id
    private Long productId;
    //支付类型，1表示支付宝，2表示微信
    private Integer type;
    //    支付金额
    private Double total;
    //    支付平台购买者用户id
    @TableField(value = "buyer_sys_id")
    private Long buyerSysId;
    //    支付平台购买用户账号
    @TableField(value = "buyer_sys_account")
    private String buyerSysAccount;
    //商品状态，1表示已支付，-1表示未支付
    private Integer status;
    //购买者收货状态，1表示确认收货，0表示未确认
    private Integer buyerStatus;
    //出售者发货状态，1表示已发货，0表示未发货
    private Integer sellerStatus;
    //买家收货时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date buyerReceiveTime;
    //卖家发货时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date sellerSendTime;
    //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    //支付时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date payTime;


}

