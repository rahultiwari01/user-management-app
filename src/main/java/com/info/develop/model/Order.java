package com.info.develop.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class Order {

    private String orderId;
    private Long customerId; // Corresponds to User ID
    private List<OrderItem> items;
    private BigDecimal totalPrice;
    private Instant orderTimestamp;

    // Default constructor for JSON deserialization
    public Order() {
        this.orderId = UUID.randomUUID().toString();
        this.orderTimestamp = Instant.now();
    }

    public Order(Long customerId, List<OrderItem> items, BigDecimal totalPrice) {
        this.orderId = UUID.randomUUID().toString();
        this.customerId = customerId;
        this.items = items;
        this.totalPrice = totalPrice;
        this.orderTimestamp = Instant.now();
    }

    // Getters and Setters
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Instant getOrderTimestamp() { return orderTimestamp; }

    public void setOrderTimestamp(Instant orderTimestamp) { this.orderTimestamp = orderTimestamp; }
}