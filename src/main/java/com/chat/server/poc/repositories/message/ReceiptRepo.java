package com.chat.server.poc.repositories.message;

import com.chat.server.poc.entities.Receipt;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReceiptRepo extends MongoRepository<Receipt, String> {
    Optional<List<Receipt>> findAllBySenderId(String receiverId);

    Optional<Receipt> findByMessageIdAndSenderIdAndReceiverId(String messageId, String senderId, String receiverId);
    void deleteAllBySenderId(String senderId);
}
