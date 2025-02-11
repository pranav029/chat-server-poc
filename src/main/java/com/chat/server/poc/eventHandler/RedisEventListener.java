package com.chat.server.poc.eventHandler;

import com.chat.server.poc.events.*;
import com.chat.server.poc.utils.AppConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RedisEventListener implements MessageListener {
    private final ObjectMapper mapper;
    private final String instanceId;
    private final ApplicationEventPublisher publisher;

    public RedisEventListener(
            ObjectMapper mapper,
            @Qualifier("instanceId") String instanceId,
            ApplicationEventPublisher publisher
    ) {
        this.mapper = mapper;
        this.instanceId = instanceId;
        this.publisher = publisher;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String redisTopic = new String(pattern);
        if (redisTopic.equals(AppConstants.READ_TOPIC))
            handleMessage(message, ReadEvent.class);
        if (redisTopic.equals(AppConstants.DELIVERED_TOPIC))
            handleMessage(message, DeliveredEvent.class);
        if (redisTopic.equals(AppConstants.SENT_TOPIC))
            handleMessage(message, SentEvent.class);
        if (redisTopic.equals(AppConstants.SEND_TOPIC))
            handleMessage(message, SendEvent.class);
    }

    private <T extends ChatEvent> void handleMessage(Message message, Class<T> type) {
        try {
            ChatEvent event = mapper.readValue(new String(message.getBody()), type);
            publisher.publishEvent(event);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
    }
}
