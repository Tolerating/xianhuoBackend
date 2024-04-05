package com.xianhuo.xianhuobackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xianhuo.xianhuobackend.entity.ChatList;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ChatListMapper extends BaseMapper<ChatList> {
    @Select("select cl.list_id, cl.link_id, cl.to_user,cl.from_user, cm.content,cm.type, cl.unread, cm.send_time, cl.from_window,cl.to_window\n" +
            "     from chat_list as cl, chat_message as cm\n" +
            "where cl.from_user =#{fromUser}  and cl.status=0 and cl.link_id = cm.link_id and cm.is_latest = 1\n" +
            " order by send_time Desc;")
    List<ChatList> getChatListByFromUser(@Param("fromUser") Long id);
}
