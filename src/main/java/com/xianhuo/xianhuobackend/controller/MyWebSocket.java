package com.xianhuo.xianhuobackend.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xianhuo.xianhuobackend.common.SocketSessions;
import com.xianhuo.xianhuobackend.config.WebSocketConfig;
import com.xianhuo.xianhuobackend.entity.ChatList;
import com.xianhuo.xianhuobackend.entity.ChatMessage;
import com.xianhuo.xianhuobackend.entity.Notice;
import com.xianhuo.xianhuobackend.entity.Users;
import com.xianhuo.xianhuobackend.service.ChatListService;
import com.xianhuo.xianhuobackend.service.ChatMessageService;
import com.xianhuo.xianhuobackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(value = "/websocket/{id}", configurator = WebSocketConfig.class)
@Component
@SuppressWarnings("all")
public class MyWebSocket {
    //用来存放每个客户端对应的MyWebSocket对象。
    private static CopyOnWriteArraySet<MyWebSocket> webSocketSet = new CopyOnWriteArraySet<MyWebSocket>();
    //用来记录username和该session进行绑定
//    private static Map<Long, Session> map = new HashMap<>();
    @Autowired
    private SocketSessions socketSessions;
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    //用户名
    private Long username;
    //聊天逻辑层service
    public static ChatMessageService chatMessageService;
    public static ChatListService chatListService;
    public static UserService userService;


    /**
     * 连接建立成功调用的方法，初始化昵称、session
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("id") Long fromUser, EndpointConfig config) {
        this.session = session;
        this.username = fromUser;
        if (socketSessions.map.get(username) == null) {
            //绑定username与session
            socketSessions.map.put(username, session);
            //加入set中
            webSocketSet.add(this);
        }

    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        socketSessions.map.remove(this.username);
        //将当前的session删除
        webSocketSet.remove(this);

    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message) throws JsonProcessingException {
        if (message.equals("heart keep")) {
            System.out.println(message);
        } else {
            //从客户端传过来的数据是json数据，所以这里使用jackson进行转换为chatMsg对象，
            ObjectMapper objectMapper = new ObjectMapper();
            try {
//                Map<String,Object> map = objectMapper.readValue(message, Map.class);
//                if(map.get("showType").equals("chat")){
//                    发送聊天消息
                ChatMessage chatMsg = objectMapper.readValue(message, ChatMessage.class);
                System.out.println(chatMsg);
                //对chatMsg进行装箱
                chatMsg.setIsLatest(1);

                Session fromSession = socketSessions.map.get(chatMsg.getFromUser());
                Session toSession = socketSessions.map.get(chatMsg.getToUser());


                //声明一个map，封装直接发送信息数据返回前端
                Map<String, Object> resultMap = new HashMap<>();
                resultMap.put("sendUser", username);
                resultMap.put("content", chatMsg.getContent());
                resultMap.put("sendTime", chatMsg.getSendTime());
                resultMap.put("avatar", chatMsg.getSenderAvatar());
                resultMap.put("senderName", chatMsg.getSenderName());
                resultMap.put("messageType",chatMsg.getType());
                resultMap.put("type", "chat");

                // 1.判断接收方的toSession是否为null，如果不是则直接发送
                if (toSession != null && toSession.isOpen()) {
                    //发送给接收者.
                    resultMap.put("notice",true);
                    toSession.getAsyncRemote().sendText(new JSONObject(resultMap).toString());
                    //发送给发送者.
                    resultMap.put("notice",false);
                    fromSession.getAsyncRemote().sendText(new JSONObject(resultMap).toString());
                }
                Users users = userService.getById(chatMsg.getToUser());
                if (users.getInChat() == 0) {
                    //对方不在线，聊天列表未读数加1
                    ChatList one = chatListService.getOne(new LambdaQueryWrapper<ChatList>()
                            .eq(ChatList::getLinkId, chatMsg.getLinkId())
                            .eq(ChatList::getFromUser, chatMsg.getToUser()));
//                    将接收者的聊天列表的status设置为0
                    chatListService.update(new LambdaUpdateWrapper<ChatList>()
                            .set(ChatList::getUnread, one.getUnread() + 1)
                            .set(ChatList::getStatus, 0)
                            .eq(ChatList::getLinkId, chatMsg.getLinkId())
                            .eq(ChatList::getFromUser, chatMsg.getToUser()));
                }
                //取消上一条消息为最新
                chatMessageService.update(new LambdaUpdateWrapper<ChatMessage>().set(ChatMessage::getIsLatest, 0).eq(ChatMessage::getLinkId, chatMsg.getLinkId()));
                //保存聊天记录信息
                chatMessageService.save(chatMsg);
//                }else{
////                    发送官方通知
//                    Notice notice = objectMapper.readValue(message, Notice.class);
//
//                }

            } catch (JsonParseException e) {
                e.printStackTrace();
            } catch (JsonMappingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }


    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    /**
     * 群发自定义消息
     */
    public void broadcast(String message) {
        for (MyWebSocket item : webSocketSet) {

            //异步发送消息.
            item.session.getAsyncRemote().sendText(message);
        }
    }
}
