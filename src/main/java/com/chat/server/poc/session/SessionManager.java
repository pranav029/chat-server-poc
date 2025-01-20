package com.chat.server.poc.session;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;


@Component
public class SessionManager {
    private static final Logger log = LoggerFactory.getLogger(SessionManager.class);
    private final SessionStorage sessionStorage;

    @Autowired
    public SessionManager(SessionStorage sessionStorage) {
        this.sessionStorage = sessionStorage;
    }

    public void addSession(WebSocketSession session) {
        synchronized (sessionStorage) {
            var userId = session.getAttributes().get("userId").toString();
            sessionStorage.saveSessionForUserId(userId, session);
            log.info("Session for user {} added", userId);
        }
    }

    public void removeSession(WebSocketSession session) {
        synchronized (sessionStorage) {
            var userId = session.getAttributes().get("userId").toString();
            sessionStorage.removeSessionForUserId(userId);
            log.info("Session for user {} removed", userId);
        }
    }
}
