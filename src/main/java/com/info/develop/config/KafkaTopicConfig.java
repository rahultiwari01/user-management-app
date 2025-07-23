package com.info.develop.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class KafkaTopicConfig {

    // Constants for services to use, ensuring type-safety and preventing typos.
    public static final String TOPIC_ORDERS = "orders";
    public static final String TOPIC_INVENTORY_UPDATES = "inventory-updates";

    private final KafkaProperties kafkaProperties;

    @Autowired
    public KafkaTopicConfig(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    @Bean
    public KafkaAdmin.NewTopics topics() {
        List<NewTopic> newTopics = kafkaProperties.getTopics().stream()
                .map(topic -> TopicBuilder.name(topic.getName())
                        .partitions(topic.getPartitions() != null ? topic.getPartitions() : 1)
                        .replicas(topic.getReplicas() != null ? topic.getReplicas() : 1)
                        .build())
                .collect(Collectors.toList());
        return new KafkaAdmin.NewTopics(newTopics.toArray(new NewTopic[0]));
    }
}