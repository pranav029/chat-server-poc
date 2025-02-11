package com.chat.server.poc.controllers;

import com.chat.server.poc.service.message.MessageDeliveryService;
import com.chat.server.poc.service.message.MessageService;
import com.chat.server.poc.session.CacheManager;
import com.chat.server.poc.session.SessionManager;
import com.netflix.appinfo.EurekaInstanceConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
@Slf4j
public class ChatSocketHandler extends TextWebSocketHandler {
    private final SessionManager sessionManager;
    private final CacheManager cacheManager;
    private final MessageDeliveryService deliveryService;
    @Autowired
    private Environment environment;

    @Autowired
    private EurekaInstanceConfig instanceConfig;

    @Autowired
    public ChatSocketHandler(SessionManager sessionManager, CacheManager cacheManager, MessageDeliveryService deliveryService) {
        this.sessionManager = sessionManager;
        this.cacheManager = cacheManager;
        this.deliveryService = deliveryService;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        log.info("Connection established " + session.getId());
        sessionManager.addSession(session);
        deliveryService.deliverMessagesToUser(sessionManager.getUserIdFromSession(session));
        session.sendMessage(new TextMessage(String.format("Connected to %s with id %s", environment.getProperty("server.port"), instanceConfig.getInstanceId())));
        cacheManager.setValue(session.getAttributes().get("userId").toString().trim(), instanceConfig.getInstanceId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        sessionManager.removeSession(session);
        cacheManager.removeKey(session.getAttributes().get("userId").toString().trim());
    }
}
