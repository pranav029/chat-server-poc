package com.chat.server.poc.eventHandler;

import com.chat.server.poc.dto.response.UserStatus;

public interface EventDispatcher {
    void dispatchReadEvent(String senderId, String receiverId);

    void dispatchDeliveredEvent(String senderId, String receiverId);

    void dispatchSentEvent(String senderId, String receiverId);

    void dispatchSendEvent(String senderId, String receiverId);
    void dispatchUserEvent(String userId, UserStatus status);
}
