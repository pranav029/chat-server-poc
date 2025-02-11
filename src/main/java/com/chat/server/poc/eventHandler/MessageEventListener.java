package com.chat.server.poc.eventHandler;

import com.chat.server.poc.events.DeliveredEvent;
import com.chat.server.poc.events.ReadEvent;
import com.chat.server.poc.events.SendEvent;
import com.chat.server.poc.events.SentEvent;
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
        deliveryService.deliverMessagesToUser(event.getReceiverId());
    }
}
