package com.info.develop.service;

import com.info.develop.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private KafkaProducerService kafkaProducerService;

    public Order processOrder(Order order) {
        // 1. Assign a unique order ID and timestamp
        order.setOrderId(java.util.UUID.randomUUID().toString());
        order.setOrderTimestamp(Instant.now());

        // 2. Perform business logic: validation, save to database, etc.
        LOGGER.info(String.format("Processing order successful. Order ID: %s", order.getOrderId()));
        // For this example, we'll assume it's saved successfully.

        // 3. Send the processed order to Kafka for downstream processing
        kafkaProducerService.sendOrder(order);
        return order;
    }
}