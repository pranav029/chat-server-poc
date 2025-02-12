package com.chat.server.poc.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class Receipt extends Message {
    @Override
    @JsonIgnore
    public MessageType getType() {
        return MessageType.RECEIPT;
    }
}
