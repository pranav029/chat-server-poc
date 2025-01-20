package com.chat.server.poc.exceptions;

public class WebSocketSessionNotFound extends Exception {
    public WebSocketSessionNotFound(String userId) {
        super(String.format("WebSocketSession not found with id %s", userId));
    }
}
