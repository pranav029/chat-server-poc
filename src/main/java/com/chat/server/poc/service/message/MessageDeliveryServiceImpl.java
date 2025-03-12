package com.chat.server.poc.service.message;

import com.chat.server.poc.dto.response.UserStatus;
import com.chat.server.poc.entities.Message;
import com.chat.server.poc.exceptions.WebSocketSessionNotFound;
import com.chat.server.poc.repositories.message.MessageRepo;
import com.chat.server.poc.session.SessionStorage;
import com.chat.server.poc.utils.MessageUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class MessageDeliveryServiceImpl implements MessageDeliveryService {
    private final MessageRepo messageRepo;
    private final SessionStorage sessionStorage;
    private final ObjectMapper mapper;

    public MessageDeliveryServiceImpl(
            MessageRepo messageRepo,
            SessionStorage sessionStorage,
            ObjectMapper mapper
    ) {
        this.messageRepo = messageRepo;
        this.sessionStorage = sessionStorage;
        this.mapper = mapper;
    }

    @Override
    public void notifyStatusForUser(UserStatus status, String userId) {
        Set<String> subscribers = sessionStorage.getSubscribers(userId);
        for (var subscriber : subscribers) {
            try {
                var session = sessionStorage.getSessionForUserId(subscriber);
                if (!session.isOpen())
                    continue;
                String payload = mapper.writeValueAsString(MessageUtils.buildUserStatusResponse(userId, status));
                TextMessage textMessage = new TextMessage(payload);
                session.sendMessage(textMessage);
                log.info("Message sent to to user {}", subscriber);
            } catch (WebSocketSessionNotFound | IOException e) {
                log.error(e.getMessage());
            }
        }
    }

    @Async
    @Override
    public void deliverMessagesToUser(String userId) {
        try {
            var userSession = sessionStorage.getSessionForUserId(userId);
            if (!userSession.isOpen()) throw new WebSocketSessionNotFound(userId);
            List<Message> messages = Stream.concat(messageRepo.getUndeliveredMessagesFor(userId).orElseGet(ArrayList::new).stream(),
                            messageRepo.getUndeliveredReceiptsFor(userId).orElseGet(ArrayList::new).stream())
                    .collect(Collectors.toList());
            if (messages.isEmpty()) {
                log.info("Empty message queue.Aborting delivery attempt");
                return;
            }
            String payload = mapper.writeValueAsString(MessageUtils.buildMessageResponse(messages));
            TextMessage textMessage = new TextMessage(payload);
            userSession.sendMessage(textMessage);
            log.info("Message sent to to user {}", userId);
            messageRepo.deleteAllReceiptsForUser(userId);
        } catch (WebSocketSessionNotFound | IOException e) {
            log.error(e.getMessage());
        }
    }
}
