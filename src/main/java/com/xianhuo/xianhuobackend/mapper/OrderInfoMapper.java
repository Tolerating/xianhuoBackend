package com.xianhuo.xianhuobackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xianhuo.xianhuobackend.entity.OrderInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * (OrderInfo)表数据库访问层
 *
 * @author makejava
 * @since 2024-03-01 22:20:06
 */
public interface OrderInfoMapper extends BaseMapper<OrderInfo> {

    @Select("select a.*,b.seller_status as seller_after_status,b.platform_status as platform_after_status,b.create_time as after_create_time,b.status as after_status from order_info as a\n" +
            "left join after_service as b on a.after_service_id = b.id\n" +
            " where a.buy_id = #{buyerId} and a.buyer_status =1 and a.seller_status =1 order by a.create_time desc;")
    List<OrderInfo> getBuyerHistory(@Param("buyerId")Long buyerId);

    @Select("select a.*,b.seller_status as seller_after_status,b.platform_status as platform_after_status,b.create_time as after_create_time,b.status as after_status from order_info as a\n" +
            "left join after_service as b on a.after_service_id = b.id\n" +
            " where a.sell_id = #{sellerId} and a.buyer_status =1 and a.seller_status =1 order by a.create_time desc;")
    List<OrderInfo> getSellerHistory(@Param("sellerId")Long sellerId);

}

