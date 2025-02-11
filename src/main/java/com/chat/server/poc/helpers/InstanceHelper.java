package com.chat.server.poc.helpers;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class InstanceHelper {
    private static InstanceHelper helper;
    @Autowired
    private DiscoveryClient discoveryClient;

    @Value("${spring.application.name}")
    private String serviceName;


    public synchronized static boolean isInstanceIsUp(String instanceId) {
        List<ServiceInstance> instances = helper.discoveryClient.getInstances(helper.serviceName);
        return instances.stream().anyMatch(instance -> instance.getInstanceId().equals(instanceId));
    }

    @PostConstruct
    void onInit() {
        helper = this;
    }

    @PreDestroy
    void onDestroy() {
        helper = null;
    }
}
