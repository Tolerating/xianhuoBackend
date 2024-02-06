package com.xianhuo.xianhuobackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xianhuo.xianhuobackend.entity.SellModeDispatchRequire;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface SellModeDispatchRequireMapper extends BaseMapper<SellModeDispatchRequire> {

    @Select("select a.*,b.name as sellName,c.name as dispatchName,d.name as productRequireName from sell_mode_dispatch_require as a\n" +
            "    left join sell_mode as b on b.id = a.sell_mode_id\n" +
            "    left join dispatch_mode as c on c.id = a.dispatch_id\n" +
            "    left join product_require as d on d.id = a.product_require_id where a.status = 1 and b.status =1 and c.status =1;")
    List<SellModeDispatchRequire> modes();
}
