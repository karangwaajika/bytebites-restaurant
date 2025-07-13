package com.lab.order_service.service.impl;

import com.lab.order_service.dto.MenuDto;
import com.lab.order_service.dto.OrderRequestDto;
import com.lab.order_service.dto.OrderResponseDto;
import com.lab.order_service.events.OrderPlacedEvent;
import com.lab.order_service.exception.MenuNotFoundException;
import com.lab.order_service.mapper.OrderMapper;
import com.lab.order_service.model.OrderEntity;
import com.lab.order_service.repository.OrderRepository;
import com.lab.order_service.service.MenuService;
import com.lab.order_service.service.OrderEventPublisher;
import com.lab.order_service.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class OrderServiceImpl implements OrderService {
    OrderRepository orderRepository;
    ModelMapper modelMapper;
    MenuService menuService;

    OrderEventPublisher orderEventPublisher;

    public OrderServiceImpl(OrderRepository orderRepository,
                            ModelMapper modelMapper, MenuService menuService,
                            OrderEventPublisher orderEventPublisher) {
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
        this.menuService = menuService;
    }

    @Override
    public OrderResponseDto order(OrderRequestDto orderRequestDto) throws Exception {
        if (!menuService.isMenuExists(orderRequestDto.getMenuId())) {
            throw new MenuNotFoundException(
                    String.format("Menu with id '%s' doesn't exist",
                            orderRequestDto.getMenuId()));
        }

//        OrderEntity orderEntity = this.modelMapper.map(orderRequestDto, OrderEntity.class);
        OrderEntity orderEntity = OrderMapper.toEntity(orderRequestDto);
        orderEntity.setId(null);
        OrderEntity savedOrder = this.orderRepository.save(orderEntity);
        MenuDto menu = menuService.findMenuById(orderRequestDto.getMenuId());

//        OrderResponseDto response = modelMapper.map(savedOrder, OrderResponseDto.class);
        OrderResponseDto response = OrderMapper.toResponseDto(savedOrder, menu);
        response.setMenu(menu);

        // publish the event
        OrderPlacedEvent event = OrderPlacedEvent.builder()
                .orderId(savedOrder.getId())
                .customerId(savedOrder.getCustomerId())
                .menuItemId(savedOrder.getMenuId())
                .placedAt(Instant.now())
                .build();

        orderEventPublisher.publishOrderPlacedEvent(event);

        return response;
    }
}
