package com.info.develop.controller;

import com.info.develop.dto.CreateOrderRequest;
import com.info.develop.model.Order;
import com.info.develop.service.AuthenticationService;
import com.info.develop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody CreateOrderRequest orderRequest) {
        try {
            // Security Best Practice: Associate the order with the authenticated user
            // to prevent one user from creating an order for another.
            // This assumes the Order model has a 'customerId' field.
            String username = authenticationService.getCurrentUsername();

            Order processedOrder = orderService.processOrder(orderRequest, username);
            return ResponseEntity.status(HttpStatus.CREATED).body(processedOrder);
        } catch (Exception e) {
            // Consider adding logging for the exception
            // Also, consider a global exception handler (@ControllerAdvice)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<Page<Order>> getOrdersForUser(Pageable pageable) {
        String username = authenticationService.getCurrentUsername();
        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Page<Order> userOrders = orderService.getOrdersForUser(username, pageable);
        return ResponseEntity.ok(userOrders);
    }
}