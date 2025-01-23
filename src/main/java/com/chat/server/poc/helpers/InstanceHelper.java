package com.chat.server.poc.helpers;

import com.chat.server.poc.dto.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.function.Consumer;

@Service
@Slf4j
public class InstanceHelper {
    private final RestTemplate restTemplate;
    private final DiscoveryClient discoveryClient;
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper mapper;

    @Value("${spring.application.name}")
    private String serviceName;

    @Autowired
    public InstanceHelper(
            RestTemplate restTemplate,
            DiscoveryClient discoveryClient,
            @Qualifier("pocRedisTemplate") RedisTemplate<String, String> redisTemplate,
            ObjectMapper mapper
    ) {
        this.restTemplate = restTemplate;
        this.discoveryClient = discoveryClient;
        this.redisTemplate = redisTemplate;
        this.mapper = mapper;
    }

    private void checkIfInstanceIsUp(String instanceId) {
        List<ServiceInstance> instances = discoveryClient.getInstances(serviceName);
        instances.stream().filter(instance -> instance.getInstanceId().equals(instanceId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(String.format("Instance with id %s not found", instanceId)));
    }

    @Async
    public void sendMessageToInstance(String instanceId, Message message, Consumer<Message> onError) {
        try {
            checkIfInstanceIsUp(instanceId);
            redisTemplate.convertAndSend(instanceId, mapper.writeValueAsString(message));
            log.info(String.format("Message with id %s sent to instance %s", message.getMessageId(), instanceId));
        } catch (Exception e) {
            log.error(e.getMessage());
            onError.accept(message);
        }
    }
}
