package com.info.develop.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemDto {
    private String productId;
    private int quantity;
    private BigDecimal price;
}