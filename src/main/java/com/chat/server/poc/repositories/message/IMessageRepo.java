package com.chat.server.poc.repositories.message;

import com.chat.server.poc.entities.ChatMessage;
import com.chat.server.poc.entities.MessageStatus;
import com.chat.server.poc.entities.Receipt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class IMessageRepo implements MessageRepo {
    private final ChatMessageRepo chatMessageRepo;
    private final ReceiptRepo receiptRepo;

    public IMessageRepo(
            ChatMessageRepo chatMessageRepo,
            ReceiptRepo receiptRepo
    ) {
        this.chatMessageRepo = chatMessageRepo;
        this.receiptRepo = receiptRepo;
    }

    @Override
    public ChatMessage save(ChatMessage message) {
        return chatMessageRepo.save(message);
    }

    @Override
    public ChatMessage updateStatus(String messageId, String receiverId, String senderId, MessageStatus status) {
        receiptRepo.findByMessageIdAndSenderIdAndReceiverId(messageId, senderId, receiverId)
                .ifPresentOrElse(receipt -> {
                    receipt.setStatus(status);
                    receiptRepo.save(receipt);
                }, () -> {
                    Receipt newReceipt = new Receipt();
                    newReceipt.setMessageId(messageId);
                    newReceipt.setSenderId(senderId);
                    newReceipt.setReceiverId(receiverId);
                    newReceipt.setCreatedAt(Instant.now().toString());
                    newReceipt.setStatus(status);
                    receiptRepo.save(newReceipt);
                });
        return null;
    }

    @Override
    public void deleteMessage(String messageId, String receiverId, String senderId) {
        chatMessageRepo.deleteByMessageIdAndSenderIdAndReceiverId(messageId, senderId, receiverId);
    }

    @Override
    public void deleteMessages(String senderId, String receiverId) {

    }

    @Override
    public List<ChatMessage> getAllMessages(String receiverId, String senderId) {
        return chatMessageRepo.findAllBySenderIdAndReceiverId(senderId, receiverId).orElseGet(ArrayList::new);
    }

    @Override
    public Optional<List<ChatMessage>> getUndeliveredMessagesFor(String userId) {
        return chatMessageRepo.findAllByReceiverId(userId);
    }

    @Override
    public Optional<List<Receipt>> getUndeliveredReceiptsFor(String userId) {
        return receiptRepo.findAllBySenderId(userId);
    }

    @Override
    public void deleteAllReceiptsForUser(String userId) {
        receiptRepo.deleteAllBySenderId(userId);
    }

    @Override
    public ChatMessage getMessage(String senderId, String receiverId, String messageId) {
        return null;
    }
}
