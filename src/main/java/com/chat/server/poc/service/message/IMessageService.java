package com.chat.server.poc.service.message;

import com.chat.server.poc.entities.ChatMessage;
import com.chat.server.poc.entities.MessageStatus;
import com.chat.server.poc.eventHandler.EventDispatcher;
import com.chat.server.poc.repositories.message.MessageRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class IMessageService implements MessageService {
    private final ObjectMapper mapper;
    private final MessageRepo messageRepo;
    private final EventDispatcher dispatcher;

    public IMessageService(
            ObjectMapper mapper,
            MessageRepo messageRepo,
            EventDispatcher dispatcher
    ) {
        this.mapper = mapper;
        this.messageRepo = messageRepo;
        this.dispatcher = dispatcher;
    }

    @Override
    public ChatMessage sendMessage(ChatMessage message) {
        message.setMessageId(UUID.randomUUID().toString());
        message.setCreatedAt(Instant.now().toString());
        message.setStatus(MessageStatus.SENT);
        ChatMessage chatMessage = messageRepo.save(message);
        log.info("Message received on server with id {}", chatMessage.getMessageId());
        updateStatus(MessageStatus.SENT, chatMessage.getSenderId(), chatMessage.getReceiverId(), chatMessage.getMessageId());
        dispatcher.dispatchSentEvent(chatMessage.getSenderId(), chatMessage.getReceiverId());
        dispatcher.dispatchSendEvent(chatMessage.getSenderId(), chatMessage.getReceiverId());
        return chatMessage;
    }

    @Override
    public void updateStatus(MessageStatus status, String senderId, String receiverId, String messageId) {
        messageRepo.updateStatus(messageId, receiverId, senderId, status);
        log.info("Receipt received with id {} & status {}", messageId, status.toString());
    }

    @Async
    @Override
    public void messageRead(String messageId, String receiverId, String senderId) {
        updateStatus(MessageStatus.READ, senderId, receiverId, messageId);
        dispatcher.dispatchReadEvent(senderId, receiverId);
    }

    @Async
    @Override
    public void messageDelivered(String messageId, String receiverId, String senderId) {
        updateStatus(MessageStatus.DELIVERED, senderId, receiverId, messageId);
        messageRepo.deleteMessage(messageId, receiverId, senderId);
        dispatcher.dispatchDeliveredEvent(senderId, receiverId);
    }

    @Async
    @Override
    public void deleteMessages(String senderId, String receiverId) {
        messageRepo.deleteMessages(senderId, receiverId);
    }

    @Override
    public List<ChatMessage> getAllMessages(String receiverId, String senderId) {
        return messageRepo.getAllMessages(receiverId, senderId);
    }
}
