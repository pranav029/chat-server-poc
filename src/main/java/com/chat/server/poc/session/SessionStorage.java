package com.chat.server.poc.session;

import com.chat.server.poc.exceptions.WebSocketSessionNotFound;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.Map;

@Component
public class SessionStorage {
    private final Map<String, WebSocketSession> sessionMap;

    public SessionStorage() {
        sessionMap = new HashMap<>();
    }

    public WebSocketSession getSessionForUserId(String userId) {
        if (!sessionMap.containsKey(userId))
            throw new WebSocketSessionNotFound(userId);
        return sessionMap.get(userId);
    }

    public void saveSessionForUserId(String userId, WebSocketSession session) {
        sessionMap.put(userId, session);
    }

    public void removeSessionForUserId(String userId) {
        sessionMap.remove(userId);
    }
}
