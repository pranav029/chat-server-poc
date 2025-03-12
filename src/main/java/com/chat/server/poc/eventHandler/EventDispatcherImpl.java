package com.chat.server.poc.eventHandler;

import com.chat.server.poc.dto.response.UserStatus;
import com.chat.server.poc.events.*;
import com.chat.server.poc.helpers.UserHelper;
import com.chat.server.poc.utils.AppConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EventDispatcherImpl implements EventDispatcher {
    private final ApplicationEventPublisher publisher;
    private final RedisTemplate<String, String> redisTemplate;
    private final UserHelper userHelper;
    private final ObjectMapper mapper;

    @Autowired
    public EventDispatcherImpl(
            ApplicationEventPublisher publisher,
            @Qualifier("pocRedisTemplate") RedisTemplate<String, String> redisTemplate,
            UserHelper userHelper,
            ObjectMapper mapper
    ) {
        this.publisher = publisher;
        this.redisTemplate = redisTemplate;
        this.userHelper = userHelper;
        this.mapper = mapper;
    }

    @Override
    public void dispatchReadEvent(String senderId, String receiverId) {
        dispatchEvent(senderId, new ReadEvent(senderId, receiverId), AppConstants.READ_TOPIC);
    }

    @Override
    public void dispatchDeliveredEvent(String senderId, String receiverId) {
        dispatchEvent(senderId, new DeliveredEvent(senderId, receiverId), AppConstants.DELIVERED_TOPIC);
    }

    @Override
    public void dispatchSentEvent(String senderId, String receiverId) {
        dispatchEvent(senderId, new SentEvent(senderId, receiverId), AppConstants.SENT_TOPIC);
    }

    @Override
    public void dispatchSendEvent(String senderId, String receiverId) {
        dispatchEvent(receiverId, new SendEvent(senderId, receiverId), AppConstants.SEND_TOPIC);
    }

    @Override
    public void dispatchUserEvent(String userId, UserStatus status) {
        try {
            redisTemplate.convertAndSend(AppConstants.USER_STATUS_TOPIC, mapper.writeValueAsString(new StatusEvent(status, userId)));
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
    }

    private void dispatchEvent(String userId, ChatEvent event, String topic) {
        if (userHelper.isUserOnline(userId)) {
            if (userHelper.isUserConnectedToSameInstance(userId))
                publisher.publishEvent(event);
            else {
                try {
                    redisTemplate.convertAndSend(topic, mapper.writeValueAsString(event));
                } catch (JsonProcessingException e) {
                    log.error(e.getMessage());
                }
            }
        } else {
            //TODO send push notification
        }
    }
}
