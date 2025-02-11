package com.chat.server.poc.dto.request;

import com.chat.server.poc.entities.MessageStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReceiptDto {
    private String messageId;
    private String senderId;
    private String receiverId;
    private MessageStatus status;
}
