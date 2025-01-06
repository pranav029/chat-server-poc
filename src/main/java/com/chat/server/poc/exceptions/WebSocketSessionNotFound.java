package com.chat.server.poc.exceptions;

public class WebSocketSessionNotFound extends RuntimeException {
    public WebSocketSessionNotFound(String userId) {
        super(String.format("WebSocketSession not found with id %s", userId));
    }
}
