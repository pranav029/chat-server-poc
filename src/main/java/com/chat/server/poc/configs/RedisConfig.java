package com.chat.server.poc.configs;

import com.chat.server.poc.eventHandler.RedisEventListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@Slf4j
public class RedisConfig {
    @Value("${redis.hostname}")
    private String hostname;
    @Value("${redis.port}")
    private Integer port;
    @Value("${redis.password}")
    private String password;

    @Value("${redis.username}")
    private String username;

    @Autowired
    @Qualifier("instanceId")
    private String instanceId;

    @Bean("factory")
    public RedisConnectionFactory provideConnectionFactory() {
        var configuration = new RedisStandaloneConfiguration();
        log.info(username);
        configuration.setHostName(hostname);
        configuration.setPort(port);
        configuration.setPassword(password);
        configuration.setUsername(username);
        return new LettuceConnectionFactory(configuration);
    }

    @Bean("pocRedisTemplate")
    public RedisTemplate<String, String> provideTemplate(@Qualifier("factory") RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(StringRedisSerializer.UTF_8);
        return template;
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(@Qualifier("delegate") MessageListenerAdapter adapter) {
        RedisMessageListenerContainer container
                = new RedisMessageListenerContainer();
        container.setConnectionFactory(provideConnectionFactory());
        container.addMessageListener(adapter, provideTopic());
        return container;
    }

    @Bean
    public ChannelTopic provideTopic() {
        return new ChannelTopic(instanceId);
    }

    @Bean("delegate")
    MessageListenerAdapter messageListener(RedisEventListener listener) {
        return new MessageListenerAdapter(listener);
    }
}
