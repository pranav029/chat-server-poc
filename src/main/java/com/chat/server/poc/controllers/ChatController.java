package com.chat.server.poc.controllers;

import com.chat.server.poc.handlers.ChatMessageHandler;
import com.chat.server.poc.dto.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("chat/message")
public class ChatController {
    private final ChatMessageHandler chatMessageHandler;

    @Autowired
    public ChatController(ChatMessageHandler chatMessageHandler) {
        this.chatMessageHandler = chatMessageHandler;
    }

    @PostMapping("/send")
    public Map<String, String> sendMessage(@RequestBody Message message) throws JsonProcessingException {
        chatMessageHandler.handleMessage(message);
        return Map.of("Success", "true");
    }

    @GetMapping("/greet")
    public String greet(){
        return "Hello";
    }
}
