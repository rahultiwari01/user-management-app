package com.info.develop.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "application.kafka")
public class KafkaProperties {

    private List<Topic> topics = new ArrayList<>();

    @Data
    public static class Topic {
        private String name;
        private Integer partitions;
        private Integer replicas;
    }
}