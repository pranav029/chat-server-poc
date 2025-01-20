package com.chat.server.poc.handlers;

import com.chat.server.poc.dto.Message;
import com.chat.server.poc.dto.MessageStatus;
import com.chat.server.poc.dto.MessageType;
import com.chat.server.poc.exceptions.WebSocketSessionNotFound;
import com.chat.server.poc.helpers.InstanceHelper;
import com.chat.server.poc.session.SessionStorage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;

import java.io.IOException;

@Component
public class ChatMessageHandler {
    private static final Logger log = LoggerFactory.getLogger(ChatMessageHandler.class);
    private final ObjectMapper mapper;
    private final SessionStorage sessionStorage;
    private final InstanceHelper instanceHelper;
    private final String instanceId;

    @Autowired
    public ChatMessageHandler(ObjectMapper mapper, SessionStorage sessionStorage, InstanceHelper instanceHelper, @Qualifier("instanceId") String instanceId) {
        this.mapper = mapper;
        this.sessionStorage = sessionStorage;
        this.instanceId = instanceId;
        this.instanceHelper = instanceHelper;
    }

    public void handleMessage(String payload) throws JsonProcessingException {
        Message message = mapper.readValue(payload, Message.class);
        try {
            if (message.getType().equals(MessageType.RECEIPT))
                sendMessage(message, message.getSenderId());
            else {
                updateStatusToSender(message, MessageStatus.SENT);
                sendMessage(message, message.getReceiverId());
                updateStatusToSender(message, MessageStatus.DELIVERED);
            }
        } catch (WebSocketSessionNotFound e) {
            log.error("Session with userId {} not found.Persisting in db for later retrieval.", message.getReceiverId());
            instanceHelper.sendMessageToInstance("", message);
            //TODO persist in db and send when online
        } catch (IOException e) {
            log.error("Error while sending message");
        }
    }

    public void handleMessage(Message message) throws JsonProcessingException {
        handleMessage(mapper.writeValueAsString(message));
    }

    private void sendMessage(Message message, String user) throws WebSocketSessionNotFound, IOException {
        var userSession = sessionStorage.getSessionForUserId(user);
        String payload = mapper.writeValueAsString(message);
        TextMessage textMessage = new TextMessage(payload);
        userSession.sendMessage(textMessage);
        log.info("Message sent to to user {}", user);
    }

    private void updateStatusToSender(Message message, MessageStatus status) {
        try {
            message.setStatus(status);
            sendMessage(message, message.getSenderId());
        } catch (WebSocketSessionNotFound e) {
            log.info("Status update failed.User {} is offline", message.getSenderId());
        } catch (Exception e) {
            log.error("Message parsing error");
        }
    }
}
