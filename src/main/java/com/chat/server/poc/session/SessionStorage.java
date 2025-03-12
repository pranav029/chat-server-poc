package com.chat.server.poc.session;

import com.chat.server.poc.exceptions.WebSocketSessionNotFound;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class SessionStorage {
    private final Map<String, WebSocketSession> sessionMap;
    private final Map<String, Set<String>> subscribers;

    public SessionStorage() {
        sessionMap = new ConcurrentHashMap<>();
        this.subscribers = new ConcurrentHashMap<>();
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
        removeSubscription(userId);
    }

    public void subscribeUserTo(String subscriberId, String userId) {
        if (subscriberId.equals(userId))
            return;
        if (!subscribers.containsKey(userId))
            subscribers.put(userId, new HashSet<>());
        subscribers.get(userId).add(subscriberId);
    }

    public Set<String> getSubscribers(String userId) {
        if (!subscribers.containsKey(userId))
            throw new RuntimeException("No subscribers");
        return subscribers.get(userId);
    }

    public void removeSubscription(String userId) {
        for (var key : subscribers.keySet())
            subscribers.get(key).remove(userId);
    }
}
