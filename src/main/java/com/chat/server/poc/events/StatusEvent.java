package com.chat.server.poc.events;

import com.chat.server.poc.dto.response.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusEvent {
    private UserStatus userStatus;
    private String userId;
}
