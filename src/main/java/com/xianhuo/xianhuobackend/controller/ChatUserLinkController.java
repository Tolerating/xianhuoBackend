package com.xianhuo.xianhuobackend.controller;



import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xianhuo.xianhuobackend.common.ResponseProcess;
import com.xianhuo.xianhuobackend.common.ResponseResult;
import com.xianhuo.xianhuobackend.entity.ChatList;
import com.xianhuo.xianhuobackend.entity.ChatUserLink;
import com.xianhuo.xianhuobackend.service.ChatListService;
import com.xianhuo.xianhuobackend.service.ChatUserLinkService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * (ChatUserLink)表控制层
 *
 * @author makejava
 * @since 2024-02-16 22:07:23
 */
@RestController
@CrossOrigin
@RequestMapping("/api")
public class ChatUserLinkController {
    /**
     * 服务对象
     */
    @Resource
    private ChatUserLinkService chatUserLinkService;
    @Resource
    private ChatListService chatListService;


    /**
     * 查询聊天关系，没有则创建（包括聊天列表）
     *
     * @param toUser
     * @return 单条数据
     */
    @GetMapping("/chatListLink")
    public ResponseResult selectOne(Long toUser,Long fromUser) {
        ChatUserLink one = chatUserLinkService.getOne(new LambdaQueryWrapper<ChatUserLink>()
                .eq(ChatUserLink::getToUser, toUser)
                .eq(ChatUserLink::getFromUser, fromUser)
                .or()
                .eq(ChatUserLink::getToUser, fromUser)
                .eq(ChatUserLink::getFromUser, toUser));
        System.out.println(one);
        if(one == null){
//            创建聊天关系表
            ChatUserLink chatUserLink = new ChatUserLink();
            chatUserLink.setFromUser(fromUser);
            chatUserLink.setToUser(toUser);
            boolean saved = chatUserLinkService.save(chatUserLink);
            if(saved){
//                查询新建的聊天关系表主键
                ChatUserLink link = chatUserLinkService.getOne(new LambdaQueryWrapper<ChatUserLink>()
                        .eq(ChatUserLink::getToUser, toUser)
                        .eq(ChatUserLink::getFromUser, fromUser)
                        .or()
                        .eq(ChatUserLink::getToUser, fromUser)
                        .eq(ChatUserLink::getFromUser, toUser));
//                创建聊天列表
                ChatList fromList = new ChatList();
                ChatList toList = new ChatList();
                fromList.setFromUser(fromUser);
                fromList.setToUser(toUser);
                fromList.setLinkId(link.getLinkId());
                fromList.setUnread(0);
                fromList.setStatus(0);
                toList.setFromUser(toUser);
                toList.setToUser(fromUser);
                toList.setLinkId(link.getLinkId());
                toList.setUnread(0);
                toList.setStatus(0);
                List<ChatList> lists = new ArrayList<>();
                lists.add(fromList);
                lists.add(toList);
                boolean b = chatListService.saveBatch(lists);
                return ResponseProcess.returnObject(link);
            }

        }
        return ResponseResult.ok(one,"success");
    }

    /**
     * 新增数据
     *
     * @param chatUserLink 实体对象
     * @return 新增结果
     */
    @PostMapping("/chatListLink")
    public ResponseResult insert(@RequestBody ChatUserLink chatUserLink) {
        boolean saved = chatUserLinkService.save(chatUserLink);
        return ResponseProcess.returnString(saved,"添加成功","添加失败");
    }
//
//    /**
//     * 修改数据
//     *
//     * @param chatUserLink 实体对象
//     * @return 修改结果
//     */
//    @PutMapping
//    public R update(@RequestBody ChatUserLink chatUserLink) {
//        return success(this.chatUserLinkService.updateById(chatUserLink));
//    }
//
//    /**
//     * 删除数据
//     *
//     * @param idList 主键结合
//     * @return 删除结果
//     */
//    @DeleteMapping
//    public R delete(@RequestParam("idList") List<Long> idList) {
//        return success(this.chatUserLinkService.removeByIds(idList));
//    }
}

