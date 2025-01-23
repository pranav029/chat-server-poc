package com.chat.server.poc.eventHandler;

import com.chat.server.poc.dto.Message;
import com.chat.server.poc.dto.MessageStatus;
import com.chat.server.poc.dto.MessageType;
import com.chat.server.poc.events.MessageDeliveredEvent;
import com.chat.server.poc.events.MessageSentEvent;
import com.chat.server.poc.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MessageEventListener {
    private final MessageService messageService;

    @Autowired
    public MessageEventListener(MessageService messageService) {
        this.messageService = messageService;
    }

    @EventListener
    public void onMessageDelivered(MessageDeliveredEvent event) {
        log.info(event.getMessage().getMessageId());
        Message message = event.getMessage();
        message.setType(MessageType.RECEIPT);
        message.setStatus(MessageStatus.DELIVERED);
        messageService.sendMessage(message);
    }

    @EventListener
    public void onMessageSent(MessageSentEvent event) {
        Message message = event.getMessage();
        message.setStatus(MessageStatus.SENT);
        message.setType(MessageType.RECEIPT);
        messageService.sendMessage(message);
        message.setType(MessageType.SEND);
        log.info(String.format("Message received on server with id %s", message.getMessageId()));
        messageService.sendMessage(message);
    }
}
