package com.xianhuo.xianhuobackend.config;

import com.xianhuo.xianhuobackend.controller.MyWebSocket;
import com.xianhuo.xianhuobackend.service.ChatListService;
import com.xianhuo.xianhuobackend.service.ChatMessageService;
import com.xianhuo.xianhuobackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

@Configuration
public class WebSocketConfig extends ServerEndpointConfig.Configurator{

    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        HttpSession session = (HttpSession) request.getHttpSession();
        sec.getUserProperties().put(HttpSession.class.getName(), session);
    }
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    @Autowired
    public void setSenderService(ChatListService chatListService, ChatMessageService chatMessageService, UserService userService){
        MyWebSocket.chatListService = chatListService;
        MyWebSocket.chatMessageService = chatMessageService;
        MyWebSocket.userService = userService;
    }

}
