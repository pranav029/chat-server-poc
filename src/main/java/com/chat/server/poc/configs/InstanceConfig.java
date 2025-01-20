package com.chat.server.poc.configs;

import com.netflix.appinfo.EurekaInstanceConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class InstanceConfig {

    @Bean("instanceId")
    public String provideInstanceId(EurekaInstanceConfig instanceConfig) {
        return instanceConfig.getInstanceId();
    }

    @Bean
    public RestTemplate provideRestTemplate() {
        return new RestTemplate();
    }
}
