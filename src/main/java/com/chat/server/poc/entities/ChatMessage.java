package com.chat.server.poc.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Version;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage extends Message {
    private String content;
    private String updatedAt;

    @Override
    public MessageType getType() {
        return MessageType.CHAT_MESSAGE;
    }
}
