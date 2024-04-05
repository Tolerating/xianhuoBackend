package com.xianhuo.xianhuobackend.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * (Product)表实体类
 *
 * @author makejava
 * @since 2024-01-13 14:40:06
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("product")
@SuppressWarnings("serial")
public class Product {
    //商品id
    @TableId(type = IdType.AUTO)
    private Long id;
    //分类id
    private Long categoryId;
//    分类名称
    @TableField(exist = false)
    private String categoryName;
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
//    发布者
    @TableField(exist = false)
    private String publisher;
    //商品要求id,以逗号分隔
    private String productRequireId;
//    商品要求名字，逗号分隔
    @TableField(exist = false)
    private String requireNames;
    //商品状态，1表示在售，0表示售出，-1表示下架
    private Integer status;
    //商品所在学校定位
    private String location;
    //运费
    private Double freight;
    //    完整地址
    private String address;
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

    public Product(Product p) {
        this.id = p.getId();
        this.categoryId = p.getCategoryId();
        this.categoryName = p.getCategoryName();
        this.detail = p.getDetail();
        this.images = p.getImages();
        this.currentPrice = p.getCurrentPrice();
        this.timeUnit = p.getTimeUnit();
        this.originPrice = p.getOriginPrice();
        this.sellModeId = p.getSellModeId();
        this.dispatchModeId = p.getDispatchModeId();
        this.userId = p.getUserId();
        this.publisher = p.getPublisher();
        this.productRequireId = p.getProductRequireId();
        this.requireNames = p.getRequireNames();
        this.status = p.getStatus();
        this.location = p.getLocation();
        this.freight = p.getFreight();
        this.address = p.getAddress();
        this.createTime = p.getCreateTime();
        this.updateTime = p.getUpdateTime();
        this.deleteTime = p.getDeleteTime();
    }
}

