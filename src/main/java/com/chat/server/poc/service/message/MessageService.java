package com.chat.server.poc.service.message;

import com.chat.server.poc.entities.ChatMessage;
import com.chat.server.poc.entities.MessageStatus;

import java.util.List;

public interface MessageService {
    ChatMessage sendMessage(ChatMessage message);

    void updateStatus(MessageStatus status, String senderId, String receiverId, String messageId);

    void messageRead(String messageId, String receiverId, String senderId);

    void messageDelivered(String messageId, String receiverId, String senderId);

    void deleteMessages(String senderId, String receiverId);

    List<ChatMessage> getAllMessages(String receiverId, String senderId);
}
