package com.chat.server.poc.helpers;

import com.chat.server.poc.session.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class UserHelper {

    private final String instanceId;
    private final CacheManager cacheManager;

    @Autowired
    public UserHelper(@Qualifier("instanceId") String instanceId, CacheManager cacheManager) {
        this.instanceId = instanceId;
        this.cacheManager = cacheManager;
    }

    public boolean isUserOnline(String userId) {
        return cacheManager.keyExists(userId);
    }

    public boolean isUserConnectedToSameInstance(String userId) {
        return getInstanceIdForUser(userId).trim().equals(instanceId);
    }

    public String getInstanceIdForUser(String userId) {
        return cacheManager.getValue(userId);
    }

    public void registerUserWithInstance(String userId) {
        cacheManager.setValue(userId, instanceId);
    }
}
