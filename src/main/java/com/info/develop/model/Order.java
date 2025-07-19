package com.info.develop.model;

import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
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