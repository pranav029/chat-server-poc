package com.chat.server.poc.utils;

import com.chat.server.poc.dto.request.ChatMessageDto;
import com.chat.server.poc.dto.response.SocketMessageDto;
import com.chat.server.poc.dto.response.SocketMessageType;
import com.chat.server.poc.dto.response.UserStatus;
import com.chat.server.poc.entities.ChatMessage;
import com.chat.server.poc.entities.Message;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class MessageUtils {

    public static SocketMessageDto buildMessageResponse(List<Message> messages) {
        SocketMessageDto socketMessageDto = new SocketMessageDto();
        socketMessageDto.setMessage(messages);
        socketMessageDto.setType(SocketMessageType.MESSAGE);
        return socketMessageDto;
    }

    public static List<SocketMessageDto> buildUserStatusResponse(String userId, UserStatus status) {
        SocketMessageDto socketMessageDto = new SocketMessageDto();
        socketMessageDto.setType(SocketMessageType.USER_STATUS);
        socketMessageDto.setUserId(userId);
        socketMessageDto.setUserStatus(status);
        return List.of(socketMessageDto);
    }

    public static ChatMessage mapFrom(ChatMessageDto dto){
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setContent(dto.getContent());
        chatMessage.setSenderId(dto.getSenderId());
        chatMessage.setReceiverId(dto.getReceiverId());
        return chatMessage;
    }
}
