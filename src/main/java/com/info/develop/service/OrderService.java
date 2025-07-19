package com.info.develop.service;

import com.info.develop.model.Order;
import com.info.develop.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Autowired
    private OrderRepository orderRepository; // Assuming an OrderRepository exists

    public Order processOrder(Order order) {
        // 1. Assign a unique order ID and timestamp
        order.setOrderId(UUID.randomUUID().toString());
        order.setOrderTimestamp(Instant.now());

        // 2. Perform business logic: validation, save to database, etc.
        LOGGER.info(String.format("Processing order for customer: %s", order.getCustomerId()));
        // The order should be persisted to a database.
        Order savedOrder = orderRepository.save(order);
        LOGGER.info(String.format("Order saved successfully. Order ID: %s", savedOrder.getOrderId()));

        // 3. Send the processed order to Kafka for downstream processing
        kafkaProducerService.sendOrder(savedOrder);
        return savedOrder;
    }

    public List<Order> getOrdersForUser(String username) {
        LOGGER.info(String.format("Retrieving orders for user: %s", username));
        // This assumes the Order entity has a 'customerId' field that is mapped to the username.
        return orderRepository.findByCustomerId(username);
    }
}