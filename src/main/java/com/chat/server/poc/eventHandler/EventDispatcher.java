package com.chat.server.poc.eventHandler;

public interface EventDispatcher {
    void dispatchReadEvent(String senderId, String receiverId);

    void dispatchDeliveredEvent(String senderId, String receiverId);

    void dispatchSentEvent(String senderId, String receiverId);

    void dispatchSendEvent(String senderId, String receiverId);
}
