package com.chat.server.poc.eventHandler;

import com.chat.server.poc.events.*;
import com.chat.server.poc.service.message.MessageDeliveryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MessageEventListener {
    private final MessageDeliveryService deliveryService;

    @Autowired
    public MessageEventListener(
            MessageDeliveryService deliveryService
    ) {
        this.deliveryService = deliveryService;
    }

    @EventListener
    public void onMessageDelivered(DeliveredEvent event) {
        deliveryService.deliverMessagesToUser(event.getSenderId());
    }

    @EventListener
    public void onMessageSent(SentEvent event) {
        deliveryService.deliverMessagesToUser(event.getSenderId());
    }

    @EventListener
    public void onReadEvent(ReadEvent event) {
        deliveryService.deliverMessagesToUser(event.getSenderId());
    }

    @EventListener
    public void onSendEvent(SendEvent event) {
        log.info("Send event for {}", event.getReceiverId());
        deliveryService.deliverMessagesToUser(event.getReceiverId());
    }

    @EventListener
    public void onUserStatusEvent(StatusEvent event) {
        deliveryService.notifyStatusForUser(event.getUserStatus(), event.getUserId());
    }
}
