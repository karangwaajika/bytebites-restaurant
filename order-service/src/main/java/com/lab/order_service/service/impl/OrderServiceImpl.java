package com.lab.order_service.service.impl;

import com.lab.order_service.dto.OrderRequestDto;
import com.lab.order_service.dto.OrderResponseDto;
import com.lab.order_service.exception.MenuNotFoundException;
import com.lab.order_service.repository.OrderRepository;
import com.lab.order_service.service.MenuService;
import com.lab.order_service.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {
    OrderRepository orderRepository;
    ModelMapper modelMapper;
    MenuService menuService;

    public OrderServiceImpl(OrderRepository orderRepository,
                            ModelMapper modelMapper, MenuService menuService) {
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
        this.menuService = menuService;
    }

    @Override
    public OrderResponseDto order(OrderRequestDto orderRequestDto) {
        if (!menuService.isMenuExists(orderRequestDto.getMenuId())) {
            throw new MenuNotFoundException(
                    String.format("Menu with id '%s' doesn't exist",
                            orderRequestDto.getMenuId()));
        }
        return null;
    }
}
