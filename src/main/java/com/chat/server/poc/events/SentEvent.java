package com.chat.server.poc.events;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class SentEvent extends ChatEvent {

    public SentEvent(String senderId, String receiverId) {
        super("SENT", senderId, receiverId);
    }
}
