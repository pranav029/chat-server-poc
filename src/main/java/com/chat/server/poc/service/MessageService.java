package com.chat.server.poc.service;

import com.chat.server.poc.dto.Message;
import com.chat.server.poc.dto.MessageType;
import com.chat.server.poc.events.MessageDeliveredEvent;
import com.chat.server.poc.events.MessageSentEvent;
import com.chat.server.poc.exceptions.WebSocketSessionNotFound;
import com.chat.server.poc.helpers.InstanceHelper;
import com.chat.server.poc.helpers.UserHelper;
import com.chat.server.poc.session.SessionStorage;
import com.chat.server.poc.utils.MessageUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;

import java.io.IOException;

@Service
@Slf4j
public class MessageService {
    private final ObjectMapper mapper;
    private final SessionStorage sessionStorage;
    private final UserHelper userHelper;
    private final InstanceHelper instanceHelper;
    private final ApplicationEventPublisher messageEventPublisher;

    public MessageService(
            SessionStorage sessionStorage,
            ObjectMapper objectMapper,
            UserHelper userHelper,
            InstanceHelper instanceHelper,
            ApplicationEventPublisher applicationEventPublisher
    ) {
        this.sessionStorage = sessionStorage;
        this.mapper = objectMapper;
        this.userHelper = userHelper;
        this.instanceHelper = instanceHelper;
        this.messageEventPublisher = applicationEventPublisher;
    }

    @Async
    public void persistInDb(Message message) {

    }

    @Async
    public void removeFromDbIfExists(Message message) {

    }

    @Async
    public void checkForUndeliveredMessages(String userId) {
        log.info(String.format("Checking for undelivered messages for user %s", userId));
    }

    @Async
    public void sendMessage(Message message) {
        try {
            if (message.getType().equals(MessageType.NEW)) {
                triggerEvent(message);
                return;
            }
            String recipientId = MessageUtils.getRecipientId(message);
            sendMessage(message, recipientId);
        } catch (WebSocketSessionNotFound | IOException e) {
            log.error(e.getMessage());
            persistInDb(message);
        }
    }


    private void sendMessage(Message message, String user) throws WebSocketSessionNotFound, IOException {
        if (!userHelper.isUserOnline(user)) throw new WebSocketSessionNotFound(user);
        if (!userHelper.isUserConnectedToSameInstance(user)) {
            String instanceId = userHelper.getInstanceIdForUser(user);
            instanceHelper.sendMessageToInstance(instanceId, message, this::persistInDb);
            return;
        }
        var userSession = sessionStorage.getSessionForUserId(user);
        if (!userSession.isOpen()) throw new WebSocketSessionNotFound(user);
        String payload = mapper.writeValueAsString(message);
        TextMessage textMessage = new TextMessage(payload);
        userSession.sendMessage(textMessage);
        log.info("Message sent to to user {}", user);
        triggerEvent(message);
    }

    private void triggerEvent(Message message) {
        if (message.getType().equals(MessageType.SEND))
            messageEventPublisher.publishEvent(new MessageDeliveredEvent(message));
        if (message.getType().equals(MessageType.NEW))
            messageEventPublisher.publishEvent(new MessageSentEvent(message));
    }
}
