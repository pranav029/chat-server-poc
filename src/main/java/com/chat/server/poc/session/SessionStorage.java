package com.chat.server.poc.session;

import com.chat.server.poc.exceptions.WebSocketSessionNotFound;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class SessionStorage {
    private final Map<String, WebSocketSession> sessionMap;

    public SessionStorage() {
        sessionMap = new HashMap<>();
    }

    public WebSocketSession getSessionForUserId(String userId) throws WebSocketSessionNotFound {
        log.info(sessionMap.toString());
        log.info(userId);
        if (!sessionMap.containsKey(userId.trim()))
            throw new WebSocketSessionNotFound(userId);
        return sessionMap.get(userId.trim());
    }

    public void saveSessionForUserId(String userId, WebSocketSession session) {
        sessionMap.put(userId.trim(), session);
    }

    public void removeSessionForUserId(String userId) {
        sessionMap.remove(userId.trim());
    }
}
