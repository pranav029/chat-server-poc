package com.chat.server.poc.helpers;

import com.chat.server.poc.dto.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.util.List;

@Service
@Slf4j
public class InstanceHelper {
    private final RestTemplate restTemplate;
    private final DiscoveryClient discoveryClient;
    private final String instanceId;

    @Value("${spring.application.name}")
    private String serviceName;

    @Autowired
    public InstanceHelper(RestTemplate restTemplate, DiscoveryClient discoveryClient, @Qualifier("instanceId") String instanceId) {
        this.restTemplate = restTemplate;
        this.discoveryClient = discoveryClient;
        this.instanceId = instanceId;
    }

    public String getUrlForInstanceId(String instanceId) throws MalformedURLException {
        List<ServiceInstance> instances = discoveryClient.getInstances(serviceName);
        var targetInstance = instances.stream().filter(instance -> instance.getInstanceId().equals(instanceId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(String.format("Instance with id %s not found", instanceId)));
        return targetInstance.getUri().toString().concat("/chat/message/send");
    }

    @Async
    public void sendMessageToInstance(String instanceI, Message message) {
        try {
            String url = getUrlForInstanceId(instanceId).replace("8086", "8087");
            restTemplate.postForEntity(url, getHttpEntityForRequest(message), String.class);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private HttpEntity<Object> getHttpEntityForRequest(Message message) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "name=random");
        return new HttpEntity<>(message, headers);
    }
}
