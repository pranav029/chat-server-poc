package com.chat.server.poc.utils;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

public class WebSocketUtil {
    public static void sendMessage(WebSocketSession session,String payload) throws IOException {
        TextMessage textMessage = new TextMessage(payload);
        session.sendMessage(textMessage);
    }
}
