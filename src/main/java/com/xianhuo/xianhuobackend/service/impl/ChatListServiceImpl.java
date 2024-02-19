package com.xianhuo.xianhuobackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xianhuo.xianhuobackend.entity.ChatList;
import com.xianhuo.xianhuobackend.mapper.ChatListMapper;
import com.xianhuo.xianhuobackend.service.ChatListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatListServiceImpl extends ServiceImpl<ChatListMapper, ChatList> implements ChatListService {

    @Autowired
    ChatListMapper chatListMapper;
    @Override
    public List<ChatList> getChatList(Long id) {
        return chatListMapper.getChatListByFromUser(id);
    }
}
