package com.chat.server.poc.controllers;


import com.chat.server.poc.dto.request.ChatMessageDto;
import com.chat.server.poc.dto.request.ReceiptDto;
import com.chat.server.poc.dto.response.SocketMessageDto;
import com.chat.server.poc.entities.MessageStatus;
import com.chat.server.poc.service.message.MessageService;
import com.chat.server.poc.utils.MessageUtils;
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
    public Map<String, String> sendMessage(@RequestBody ChatMessageDto message) throws JsonProcessingException {
        messageService.sendMessage(MessageUtils.mapFrom(message));
        return Map.of("Success", "true");
    }

    @GetMapping("pending/{userId}")
    public List<SocketMessageDto> getAllPendingMessages(@PathVariable String userId) {
        return List.of();
    }

    @PostMapping("/receipt")
    public Map<String, String> receipt(@RequestBody ReceiptDto receiptDto) {
        if (receiptDto.getStatus().equals(MessageStatus.DELIVERED))
            messageService.messageDelivered(receiptDto.getMessageId(), receiptDto.getReceiverId(), receiptDto.getSenderId());
        if (receiptDto.getStatus().equals(MessageStatus.READ))
            messageService.messageRead(receiptDto.getMessageId(), receiptDto.getReceiverId(), receiptDto.getSenderId());
        return Map.of("Success", "true");
    }

    @GetMapping("/greet")
    public String greet() {
        return "Hello from " + environment.getProperty("server.port");
    }
}
