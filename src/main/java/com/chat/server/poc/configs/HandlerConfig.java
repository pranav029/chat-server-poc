package com.chat.server.poc.configs;

import com.chat.server.poc.handlers.ChatMessageHandler;
import com.chat.server.poc.session.SessionManager;
import com.chat.server.poc.handlers.ChatSocketHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HandlerConfig {
    @Bean
    public ObjectMapper provideMapper() {
        return new ObjectMapper();
    }

    @Bean
    public ChatSocketHandler provideChatSocketHandler(SessionManager sessionManager, ChatMessageHandler chatMessageHandler) {
        return new ChatSocketHandler(sessionManager, chatMessageHandler);
    }
}
