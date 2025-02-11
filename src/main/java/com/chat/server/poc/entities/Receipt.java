package com.chat.server.poc.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Receipt extends Message {
    @Override
    public MessageType getType() {
        return MessageType.RECEIPT;
    }
}
