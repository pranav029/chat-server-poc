package com.chat.server.poc.utils;

import com.chat.server.poc.dto.Message;
import com.chat.server.poc.dto.MessageType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageUtils {
    public static String getRecipientId(Message message) {
        return message.getType().equals(MessageType.RECEIPT) ? message.getSenderId() : message.getReceiverId();
    }

    public static Message deepCopy(Message message, ObjectMapper mapper) throws JsonProcessingException {
        return mapper.convertValue(mapper.writeValueAsString(message), Message.class);
    }
}
