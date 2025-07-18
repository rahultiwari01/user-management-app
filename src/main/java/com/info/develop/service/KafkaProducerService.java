package com.info.develop.service;

import com.info.develop.config.KafkaTopicConfig;
import com.info.develop.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducerService.class);

    @Autowired
    private KafkaTemplate<String, Order> kafkaTemplate;

    public void sendOrder(Order order) {
        try {
            LOGGER.info(String.format("Sending order to Kafka topic: %s, Order ID: %s", KafkaTopicConfig.ORDERS_TOPIC, order.getOrderId()));
            kafkaTemplate.send(KafkaTopicConfig.ORDERS_TOPIC, order.getOrderId(), order);
        } catch (Exception e) {
            LOGGER.error("Error sending order to Kafka", e);
        }
    }
}