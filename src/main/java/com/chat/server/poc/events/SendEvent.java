package com.chat.server.poc.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class SendEvent extends ChatEvent {
    public SendEvent(String senderId, String receiverId) {
        super("SEND", senderId, receiverId);
    }
}
