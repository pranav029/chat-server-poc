package com.chat.server.poc.controllers;


import com.chat.server.poc.dto.Message;
import com.chat.server.poc.service.MessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("chat/message")
public class ChatController {
    private final MessageService messageService;
    @Autowired
    private Environment environment;

    @Autowired
    public ChatController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/send")
    public Map<String, String> sendMessage(@RequestBody Message message) throws JsonProcessingException {
        messageService.sendMessage(message);
        return Map.of("Success", "true");
    }

    @GetMapping("pending/{userId}")
    public List<Message> getAllPendingMessages(@PathVariable String userId) {
        return List.of();
    }

    @GetMapping("/greet")
    public String greet() {
        return "Hello from " + environment.getProperty("server.port");
    }
}
