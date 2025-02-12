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
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class IMessageDeliveryService implements MessageDeliveryService {
    private final MessageRepo messageRepo;
    private final SessionStorage sessionStorage;
    private final ObjectMapper mapper;

    public IMessageDeliveryService(
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
        //TODO find all users subscribedTo to this use for this instance and update
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
            if (messages.isEmpty())
                return;
            String payload = mapper.writeValueAsString(MessageUtils.messageResponse(messages));
            TextMessage textMessage = new TextMessage(payload);
            userSession.sendMessage(textMessage);
            log.info("Message sent to to user {}", userId);
            messageRepo.deleteAllReceiptsForUser(userId);
        } catch (WebSocketSessionNotFound | IOException e) {
            log.error(e.getMessage());
        }
    }
}
