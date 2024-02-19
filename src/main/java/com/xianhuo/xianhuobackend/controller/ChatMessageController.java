package com.xianhuo.xianhuobackend.controller;



import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xianhuo.xianhuobackend.common.ResponseProcess;
import com.xianhuo.xianhuobackend.common.ResponseResult;
import com.xianhuo.xianhuobackend.entity.ChatMessage;
import com.xianhuo.xianhuobackend.entity.Product;
import com.xianhuo.xianhuobackend.service.ChatMessageService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * (ChatMessage)表控制层
 *
 * @author makejava
 * @since 2024-02-16 22:07:23
 */
@RestController
@CrossOrigin
@RequestMapping("/api")
public class ChatMessageController {
    /**
     * 服务对象
     */
    @Resource
    private ChatMessageService chatMessageService;

//    分页获取聊天信息
    @GetMapping("/chatMessage")
    public ResponseResult getChatMessageByLinkId(Long current,Long size,Long linkId){
        Page<ChatMessage> page = new Page<>(current,size);
        Page<ChatMessage> paged = chatMessageService.page(page, new LambdaQueryWrapper<ChatMessage>()
                .eq(ChatMessage::getLinkId,linkId)
                .orderByDesc(ChatMessage::getSendTime));
        return ResponseProcess.returnObject(paged);
    }

}

