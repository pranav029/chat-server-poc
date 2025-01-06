package com.chat.server.poc.configs;

import com.chat.server.poc.session.SessionHandshakeInterceptor;
import com.chat.server.poc.handlers.ChatSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class SocketConfig implements WebSocketConfigurer {

    private final ChatSocketHandler chatSocketHandler;

    @Autowired
    public SocketConfig(ChatSocketHandler chatSocketHandler) {
        this.chatSocketHandler = chatSocketHandler;
    }


    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
//        DefaultHandshakeHandler handler = new DefaultHandshakeHandler();
//        handler.setSupportedProtocols("Id");
        registry.addHandler(chatSocketHandler, "/socket")
                .addInterceptors(new SessionHandshakeInterceptor())
//                .setHandshakeHandler(handler)
                .setAllowedOriginPatterns("*");
    }

}
