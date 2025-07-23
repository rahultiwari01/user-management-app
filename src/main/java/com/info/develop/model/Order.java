package com.info.develop.model;

import lombok.Data;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.List;

@Data
@Entity
@Table(name = "orders")
public class Order {

    @Id
    private String orderId;

    private String customerId; // Links the order to the user (username)
    private Instant orderTimestamp;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id") // Creates a foreign key in the order_item table
    private List<OrderItem> items;
}