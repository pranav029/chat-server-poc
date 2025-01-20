package com.chat.server.poc.helpers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class UserHelper {

    private final String instanceId;

    @Autowired
    public UserHelper(@Qualifier("instanceId") String instanceId) {
        this.instanceId = instanceId;
    }

    public boolean isUserOnline(String userId) {
        return false;
    }

    public boolean isUserConnectedToSameInstance(String userId) {
        return getInstanceIdForUser(userId).trim().equals(instanceId);
    }

    public String getInstanceIdForUser(String userId) {
        return null;
    }

    public void registerUserWithInstance(String userId) {

    }
}
