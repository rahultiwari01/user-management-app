package com.info.develop.dto;

import lombok.Data;

import java.util.List;

@Data
public class CreateOrderRequest {
    // Note: customerId is not included. It will be set from the authenticated user's JWT claims.
    private List<OrderItemDto> items;
}