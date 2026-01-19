package com.mzuha.configservice.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Value("${app.kafka.topics.config-updates}")
    private String updateTopic;

    @Bean
    public NewTopic configUpdatesTopic() {
        return TopicBuilder.name(updateTopic)
                .partitions(3)
                .replicas(1)
                .build();
    }
}
