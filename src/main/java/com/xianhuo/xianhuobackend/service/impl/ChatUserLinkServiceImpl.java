package com.xianhuo.xianhuobackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xianhuo.xianhuobackend.entity.ChatUserLink;
import com.xianhuo.xianhuobackend.mapper.ChatUserLinkMapper;
import com.xianhuo.xianhuobackend.service.ChatUserLinkService;
import org.springframework.stereotype.Service;

@Service
public class ChatUserLinkServiceImpl extends ServiceImpl<ChatUserLinkMapper, ChatUserLink> implements ChatUserLinkService {
}
