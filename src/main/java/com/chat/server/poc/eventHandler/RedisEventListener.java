package com.chat.server.poc.eventHandler;

import com.chat.server.poc.service.MessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RedisEventListener implements MessageListener {
    private final ObjectMapper mapper;
    private final MessageService messageService;

    public RedisEventListener(
            ObjectMapper mapper,
            MessageService messageService
    ) {
        this.mapper = mapper;
        this.messageService = messageService;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            com.chat.server.poc.dto.Message receivedMessage = mapper.readValue(new String(message.getBody()), com.chat.server.poc.dto.Message.class);
            messageService.sendMessage(receivedMessage);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
    }
}
