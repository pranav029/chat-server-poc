package com.chat.server.poc.events;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ReadEvent extends ChatEvent {
    public ReadEvent(String senderId, String receiverId) {
        super("READ", senderId, receiverId);
    }
}
