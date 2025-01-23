package com.chat.server.poc.events;

import com.chat.server.poc.dto.Message;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class MessageSentEvent {
    private Message message;
}
