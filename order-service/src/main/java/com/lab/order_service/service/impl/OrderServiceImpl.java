package com.lab.order_service.service.impl;

import com.lab.order_service.dto.OrderRequestDto;
import com.lab.order_service.dto.OrderResponseDto;
import com.lab.order_service.repository.OrderRepository;
import com.lab.order_service.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {
    OrderRepository orderRepository;
    ModelMapper modelMapper;

    public OrderServiceImpl(OrderRepository orderRepository,
                            ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public OrderResponseDto order(OrderRequestDto orderRequestDto) {
        return null;
    }
}
