package com.xianhuo.xianhuobackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xianhuo.xianhuobackend.entity.Favourite;
import com.xianhuo.xianhuobackend.entity.Product;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface FavouriteMapper extends BaseMapper<Favourite> {
    @Select("select p.* from favourite as f,product as p where f.user_id = #{userId} and f.status = 0 and f.product_id = p.id")
    List<Product> favouriteProductByUserId(@Param("userId")Long userId);
}
