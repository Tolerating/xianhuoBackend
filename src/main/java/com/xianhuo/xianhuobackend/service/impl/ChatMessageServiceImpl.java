package com.xianhuo.xianhuobackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xianhuo.xianhuobackend.entity.ChatMessage;
import com.xianhuo.xianhuobackend.mapper.ChatMessageMapper;
import com.xianhuo.xianhuobackend.service.ChatMessageService;
import org.springframework.stereotype.Service;

@Service
public class ChatMessageServiceImpl extends ServiceImpl<ChatMessageMapper, ChatMessage> implements ChatMessageService {
}
