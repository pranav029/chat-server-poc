package com.chat.server.poc.handlers;

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
    private final ChatMessageHandler chatMessageHandler;
    @Autowired
    private Environment environment;

    @Autowired
    private EurekaInstanceConfig instanceConfig;

    @Autowired
    public ChatSocketHandler(SessionManager sessionManager, ChatMessageHandler chatMessageHandler) {
        this.sessionManager = sessionManager;
        this.chatMessageHandler = chatMessageHandler;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
        log.info("Received message {}", message.getPayload());
        chatMessageHandler.handleMessage(message.getPayload());
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        log.info("Connection established " + session.getId());
        sessionManager.addSession(session);
        //TODO USER IS ONLINE CHECK FOR UNDELIVERED MESSAGES
        session.sendMessage(new TextMessage(String.format("Connected to %s with id %s", environment.getProperty("server.port"), instanceConfig.getInstanceId())));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        sessionManager.removeSession(session);
    }
}
