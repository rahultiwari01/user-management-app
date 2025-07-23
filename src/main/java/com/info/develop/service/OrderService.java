package com.info.develop.service;

import com.info.develop.config.KafkaTopicConfig;
import com.info.develop.dto.CreateOrderRequest;
import com.info.develop.mapper.OrderMapper;
import com.info.develop.model.Order;
import com.info.develop.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Autowired
    private OrderRepository orderRepository; // Assuming an OrderRepository exists

    @Autowired
    private OrderMapper orderMapper;

    public Order processOrder(CreateOrderRequest orderRequest, String username) {
        Order order = orderMapper.toEntity(orderRequest);

        // 1. Assign a unique order ID and timestamp
        order.setOrderId(UUID.randomUUID().toString());
        order.setOrderTimestamp(Instant.now());
        order.setCustomerId(username);

        // 2. Perform business logic: validation, save to database, etc.
        LOGGER.info(String.format("Processing order for customer: %s", order.getCustomerId()));
        // The order should be persisted to a database.
        Order savedOrder = orderRepository.save(order);
        LOGGER.info(String.format("Order saved successfully. Order ID: %s", savedOrder.getOrderId()));

        // 3. Send the processed order to Kafka for downstream processing
        kafkaProducerService.sendMessage(KafkaTopicConfig.TOPIC_ORDERS, savedOrder.getCustomerId(), savedOrder);
        return savedOrder;
    }

    public Page<Order> getOrdersForUser(String username, Pageable pageable) {
        LOGGER.info(String.format("Retrieving orders for user: %s", username));
        // This assumes the Order entity has a 'customerId' field that is mapped to the username.
        return orderRepository.findByCustomerId(username, pageable);
    }
}