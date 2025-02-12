package com.chat.server.poc.repositories.message;

import com.chat.server.poc.entities.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatMessageRepo extends MongoRepository<ChatMessage, String> {
    Optional<List<ChatMessage>> findAllBySenderIdAndReceiverId(String senderId, String receiverId);

    Optional<List<ChatMessage>> findAllByReceiverId(String receiverId);

    void deleteByMessageIdAndSenderIdAndReceiverId(String messageId, String senderId, String receiverId);
}
