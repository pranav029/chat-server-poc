package com.chat.server.poc.service.message;

import com.chat.server.poc.dto.response.UserStatus;

public interface MessageDeliveryService {
    void deliverMessagesToUser(String userId);

    void notifyStatusForUser(UserStatus status, String userId);
}
