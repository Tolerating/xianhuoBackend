package com.xianhuo.xianhuobackend.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * 投诉表(Complain)表实体类
 *
 * @author makejava
 * @since 2024-03-12 21:28:14
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@TableName("complain")
public class Complain extends Model<Complain>{
    //投诉表主键
    @TableId(type = IdType.AUTO)
    private Long id;
    //投诉人id
    private Long complainantId;
    //投诉原因
    private String complainantCause;
    //被投诉的商品或帖子
    private Long complainantSubject;
    //被投诉人id
    private Long sellerId;
    //    投诉人
    @TableField(exist = false)
    private String complaintName;
    //    发帖人
    @TableField(exist = false)
    private String sellerName;
    //投诉类别，1表示商品，0表示求购帖子
    private Integer type;
    //处理人id
    private Long dealUser;
//    处理人名字
    @TableField(exist = false)
    private String dealUserName;
    //处理方式描述
    private String dealMethod;
    //投诉时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    //处理时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dealTime;

//    public Complain(Long id, Long complainantId, String complainantCause, Long complainantSubject, Long sellerId,Integer type, Long dealUser, String dealMethod, Date createTime, Date dealTime) {
//        this.id = id;
//        this.complainantId = complainantId;
//        this.complainantCause = complainantCause;
//        this.complainantSubject = complainantSubject;
//        this.sellerId = sellerId;
//        this.type = type;
//        this.dealUser = dealUser;
//        this.dealMethod = dealMethod;
//        this.createTime = createTime;
//        this.dealTime = dealTime;
//    }

    public Complain(Complain complain) {
        this.id = complain.getId();
        this.complainantId = complain.getComplainantId();
        this.complainantCause = complain.getComplainantCause();
        this.complainantSubject = complain.getComplainantSubject();
        this.sellerId = complain.getSellerId();
        this.complaintName = complain.getComplaintName();
        this.sellerName = complain.getSellerName();
        this.type = complain.getType();
        this.dealUser = complain.getDealUser();
        this.dealMethod = complain.getDealMethod();
        this.createTime = complain.getCreateTime();
        this.dealTime = complain.getDealTime();
    }
}

