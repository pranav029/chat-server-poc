package com.chat.server.poc.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage extends Message {
    private String content;
    private String updatedAt;

    @Override
    @JsonIgnore
    public MessageType getType() {
        return MessageType.CHAT_MESSAGE;
    }
}
