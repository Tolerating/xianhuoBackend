package com.xianhuo.xianhuobackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xianhuo.xianhuobackend.entity.ChatList;

import java.util.List;

/**
 * (ChatList)表服务接口
 *
 * @author makejava
 * @since 2024-02-16 22:07:23
 */
public interface ChatListService extends IService<ChatList> {
    List<ChatList> getChatList(Long id);
}

