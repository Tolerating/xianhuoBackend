package com.xianhuo.xianhuobackend.common;

import org.springframework.stereotype.Component;

import javax.websocket.Session;
import java.util.HashMap;
import java.util.Map;

@Component
public class SocketSessions {
    public static Map<Long, Session> map = new HashMap<>();
}
