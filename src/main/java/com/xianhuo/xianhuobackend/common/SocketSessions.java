package com.xianhuo.xianhuobackend.common;

import com.alibaba.fastjson.JSONObject;
import com.xianhuo.xianhuobackend.controller.AdminController;
import org.springframework.stereotype.Component;

import javax.websocket.Session;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class SocketSessions {
    public static enum messageType {
        COMPLAIN,
        PRODUCT
    }
    public static Map<Long, Session> map = new HashMap<>();

    public static Map<String,Object> createMessageTemplate(String title, String msg, Date dealTime, Long postId, SocketSessions.messageType type){
        Map<String, Object> messageMap = new HashMap<>();
        messageMap.put("title", title);
        messageMap.put("content", msg);
        messageMap.put("dealTime", dealTime);
        messageMap.put("Id", postId);
        messageMap.put("type", type.toString().toLowerCase());
        return messageMap;
    }
    public static void sendMsg(Long userId, Map<String, Object> message) {
        Session sendSession = SocketSessions.map.get(userId);
        JSONObject json = new JSONObject(message);
        if (sendSession != null && sendSession.isOpen()) {
            sendSession.getAsyncRemote().sendText(json.toString());
        }

    }
}
