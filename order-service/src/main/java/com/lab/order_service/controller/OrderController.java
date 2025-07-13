package com.lab.order_service.controller;

import com.lab.order_service.dto.OrderRequestDto;
import com.lab.order_service.dto.OrderResponseDto;
import com.lab.order_service.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("order")
public class OrderController {
    OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/add")
    @Operation(summary = "Add order",
            description = "This request inserts a order to the database and returns " +
                          "the inserted order ")
    public ResponseEntity<OrderResponseDto> placeOrder(
            @RequestBody OrderRequestDto orderRequestDto
    ) throws Exception {
        OrderResponseDto savedUser = this.orderService.order(orderRequestDto);
        return ResponseEntity.ok(savedUser);
    }
}
