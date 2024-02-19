package com.xianhuo.xianhuobackend.controller;


import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.xianhuo.xianhuobackend.common.ResponseProcess;
import com.xianhuo.xianhuobackend.common.ResponseResult;
import com.xianhuo.xianhuobackend.entity.ChatList;
import com.xianhuo.xianhuobackend.entity.Users;
import com.xianhuo.xianhuobackend.service.ChatListService;
import com.xianhuo.xianhuobackend.service.UserService;
import com.xianhuo.xianhuobackend.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * (ChatList)表控制层
 *
 * @author makejava
 * @since 2024-02-16 22:07:23
 */
@RestController
@CrossOrigin
@RequestMapping("/api")
public class ChatListController {
    /**
     * 服务对象
     */
    @Resource
    private ChatListService chatListService;
    @Autowired
    private UserService userService;
    @Autowired
    HttpServletRequest httpServletRequest;

    /**
     * 获取当前用户的所有会话列表
     *
     * @return 所有数据
     */
    @GetMapping("/chatlists")
    public ResponseResult selectAll() {
        String authorization = httpServletRequest.getHeader("authorization");
        String id = JWTUtil.parseJWT(authorization).getId();
        List<ChatList> list = chatListService.getChatList(Long.valueOf(id));
        if(!list.isEmpty()){
            for (ChatList chatList : list) {
                Users from = userService.getById(chatList.getFromUser());
                Users to = userService.getById(chatList.getToUser());
                chatList.setFromUserPicture(from.getAvatar());
                chatList.setFromUserName(from.getName());
                chatList.setToUserPicture(to.getAvatar());
                chatList.setToUserName(to.getName());
            }
        }
        return ResponseResult.ok(list,"success");
    }

    /**
     * 新增数据
     *
     * @param chatList 实体对象
     * @return 新增结果
     */
    @PostMapping("/chatlist")
    public ResponseResult insert(@RequestBody ChatList chatList) {
        boolean saved = chatListService.save(chatList);
        return ResponseProcess.returnString(saved,"添加成功","添加失败");
    }

    /**
     * 修改未读数量
     *
     * @param fromUser 发送者id
     * @param linkId 聊天关系id
     * @return 修改结果
     */
    @GetMapping("/chatlist/status")
    public ResponseResult update(Long fromUser,Long linkId) {
        boolean updated = chatListService
                .update(new LambdaUpdateWrapper<ChatList>()
                        .set(ChatList::getUnread,0)
                        .eq(ChatList::getFromUser,fromUser)
                        .eq(ChatList::getLinkId,linkId));
        return  ResponseProcess.returnString(updated,"更改成功","更改失败");
    }

    /**
     * 删除数据，修改状态status为1
     *
     * @param id 主键
     * @return 删除结果
     */
    @DeleteMapping("/chatlist/{listId}")
    public ResponseResult delete(@PathVariable("listId") Long id) {
        boolean update = chatListService.update(new LambdaUpdateWrapper<ChatList>()
                .set(ChatList::getStatus, 1)
                .eq(ChatList::getListId, id));
        return ResponseProcess.returnString(update,"删除成功","删除失败");
    }
}

