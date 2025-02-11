package com.chat.server.poc.dto.response;

import com.chat.server.poc.entities.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SocketMessageDto {
    SocketMessageType type;
    List<Message> message;
    UserStatus userStatus;
    String userId;
}
