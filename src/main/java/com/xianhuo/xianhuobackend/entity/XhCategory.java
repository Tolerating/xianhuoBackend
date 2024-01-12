package com.xianhuo.xianhuobackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


/**
 * (XhCategory)实体类
 *
 * @author makejava
 * @since 2024-01-11 09:57:29
 */
@Data
@TableName("xh_category")
public class XhCategory {
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 分类名称
     */
    private String name;

}

