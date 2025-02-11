package com.chat.server.poc.events;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class DeliveredEvent extends ChatEvent {

    public DeliveredEvent(String senderId, String receiverId) {
        super("DELIVERED", senderId, receiverId);
    }
}
