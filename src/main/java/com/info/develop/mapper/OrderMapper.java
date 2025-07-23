package com.info.develop.mapper;

import com.info.develop.dto.CreateOrderRequest;
import com.info.develop.dto.OrderItemDto;
import com.info.develop.model.Order;
import com.info.develop.model.OrderItem;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class OrderMapper {

    public Order toEntity(CreateOrderRequest request) {
        if (request == null) {
            return null;
        }
        Order order = new Order();
        order.setItems(request.getItems().stream()
                .map(this::toEntity)
                .collect(Collectors.toList()));
        return order;
    }

    private OrderItem toEntity(OrderItemDto dto) {
        OrderItem item = new OrderItem();
        item.setProductId(dto.getProductId());
        item.setQuantity(dto.getQuantity());
        item.setPrice(dto.getPrice());
        return item;
    }
}