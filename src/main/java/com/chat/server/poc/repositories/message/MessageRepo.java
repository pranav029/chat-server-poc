package com.chat.server.poc.repositories.message;

import com.chat.server.poc.entities.ChatMessage;
import com.chat.server.poc.entities.Message;
import com.chat.server.poc.entities.MessageStatus;
import com.chat.server.poc.entities.Receipt;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface MessageRepo {
    ChatMessage save(ChatMessage message);

    ChatMessage updateStatus(String messageId, String receiverId, String senderId, MessageStatus status);

    void deleteMessage(String messageId, String receiverId, String senderId);

    void deleteMessages(String senderId, String receiverId);

    List<ChatMessage> getAllMessages(String receiverId, String senderId);

    Optional<List<ChatMessage>> getUndeliveredMessagesFor(String userId);

    Optional<List<Receipt>> getUndeliveredReceiptsFor(String userId);

    void deleteAllReceiptsForUser(String userId);

    ChatMessage getMessage(String senderId, String receiverId, String messageId);
}
