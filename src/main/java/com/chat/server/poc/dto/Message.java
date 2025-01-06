package com.chat.server.poc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Message {
    private String messageId;
    private String senderId;
    private String receiverId;
    private String content;
    private String timestamp;
    private MessageType type;
    private MessageStatus status;
}
