package com.xianhuo.xianhuobackend.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * (Product)表实体类
 *
 * @author makejava
 * @since 2024-01-13 14:40:06
 */
@Data
@TableName("product")
@SuppressWarnings("serial")
public class Product {
    //商品id
    @TableId(type = IdType.AUTO)
    private Long id;
    //分类id
    private Long categoryId;
    //商品详情
    private String detail;
    //商品图片，以逗号分隔
    private String images;
    //商品价格，保留两位小数
    private Double currentPrice;
    //物品出租时间计量单位
    private String timeUnit;
    //商品原价
    private Double originPrice;
    //出售方式id
    private Long sellModeId;
    //发货方式id
    private Long dispatchModeId;
    //发布者id
    private Long userId;
    //商品要求id,以逗号分隔
    private String productRequireId;
    //商品状态，1表示在售，0表示售出，-1表示下架
    private Integer status;
    //商品所在学校定位
    private String location;
    //运费
    private Double freight;
    //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    //更新时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    //删除时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deleteTime;


}

