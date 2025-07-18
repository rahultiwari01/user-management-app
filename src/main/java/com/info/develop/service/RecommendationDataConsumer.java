package com.info.develop.service;

import com.info.develop.config.KafkaTopicConfig;
import com.info.develop.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class RecommendationDataConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecommendationDataConsumer.class);

    @KafkaListener(topics = KafkaTopicConfig.ORDERS_TOPIC, groupId = "${spring.kafka.consumer.group-id}")
    public void consumeOrder(Order order) {
        LOGGER.info(String.format("Consumed order message -> Order ID: %s", order.getOrderId()));

        // --- Recommendation Engine Logic Goes Here ---
        // 1. Extract relevant features from the order:
        //    - customerId = order.getCustomerId()
        //    - products = order.getItems().stream().map(OrderItem::getProductId).collect(Collectors.toList())
        //
        // 2. Update user profile: Store purchase history for the customer.
        // 3. Update product-product similarity models (e.g., users who bought X also bought Y).
        // 4. In a real-world scenario, you might send this data to another database, a data lake,
        //    or a machine learning service for model training/updating.
        LOGGER.info(String.format("Processing order %s for recommendation engine.", order.getOrderId()));
    }
}