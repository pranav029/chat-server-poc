package com.chat.server.poc.events;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = SentEvent.class, name = "SENT"),
        @JsonSubTypes.Type(value = SendEvent.class, name = "SEND"),
        @JsonSubTypes.Type(value = DeliveredEvent.class, name = "DELIVERED"),
        @JsonSubTypes.Type(value = ReadEvent.class, name = "READ")
})
public abstract class ChatEvent {
    private String type;
    private String senderId;
    private String receiverId;
}
