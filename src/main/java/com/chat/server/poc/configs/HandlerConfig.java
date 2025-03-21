package com.chat.server.poc.configs;

import com.chat.server.poc.controllers.ChatSocketHandler;
import com.chat.server.poc.service.message.MessageDeliveryService;
import com.chat.server.poc.service.message.MessageService;
import com.chat.server.poc.session.CacheManager;
import com.chat.server.poc.session.SessionManager;
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
    public ChatSocketHandler provideChatSocketHandler(SessionManager sessionManager, CacheManager cacheManager, MessageDeliveryService service) {
        return new ChatSocketHandler(sessionManager, cacheManager, service);
    }
}
