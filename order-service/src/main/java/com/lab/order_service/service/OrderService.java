package com.lab.order_service.service;

import com.lab.order_service.dto.OrderRequestDto;
import com.lab.order_service.dto.OrderResponseDto;

public interface OrderService {
    OrderResponseDto order(OrderRequestDto orderRequestDto);
}
