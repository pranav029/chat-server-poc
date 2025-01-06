package com.chat.server.poc.handlers;

import com.chat.server.poc.exceptions.WebSocketSessionNotFound;
import com.chat.server.poc.dto.Message;
import com.chat.server.poc.dto.MessageStatus;
import com.chat.server.poc.dto.MessageType;
import com.chat.server.poc.session.SessionStorage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

@Component
@Slf4j
public class ChatMessageHandler {
    private final ObjectMapper mapper;
    private final SessionStorage sessionStorage;

    @Autowired
    public ChatMessageHandler(ObjectMapper mapper, SessionStorage sessionStorage) {
        this.mapper = mapper;
        this.sessionStorage = sessionStorage;
    }

    public void handleMessage(String payload) throws JsonProcessingException {
        Message message = mapper.readValue(payload, Message.class);
        var receiverId = message.getReceiverId();
        var senderId = message.getSenderId();
        try {
            var senderSession = sessionStorage.getSessionForUserId(senderId);
            var receiverSession = sessionStorage.getSessionForUserId(receiverId);
            if (message.getType().equals(MessageType.UPDATE)) {
                sendMessage(payload, senderSession);
            } else {
                message.setStatus(MessageStatus.SENT);
                sendMessage(mapper.writeValueAsString(message), senderSession);
                sendMessage(mapper.writeValueAsString(message), receiverSession);
                message.setStatus(MessageStatus.DELIVERED);
                sendMessage(mapper.writeValueAsString(message), senderSession);
            }
        } catch (WebSocketSessionNotFound e) {
            log.error("Session with userId {} not found", receiverId);
            //TODO persist in db and send when online
        } catch (IOException e) {
            log.error("Error while sending message");
        }
    }

    public void handleMessage(Message message) throws JsonProcessingException {
        handleMessage(mapper.writeValueAsString(message));
    }


    private void sendMessage(String message, WebSocketSession session) throws IOException {
        TextMessage textMessage = new TextMessage(message);
        session.sendMessage(textMessage);
    }
}
