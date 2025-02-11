package com.chat.server.poc.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChatMessageDto {
    private String content;
    private String senderId;
    private String receiverId;
}
