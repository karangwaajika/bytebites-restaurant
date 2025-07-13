package com.lab.order_service.mapper;

import com.lab.order_service.dto.MenuDto;
import com.lab.order_service.dto.OrderRequestDto;
import com.lab.order_service.dto.OrderResponseDto;
import com.lab.order_service.model.OrderEntity;

public class OrderMapper {
    public static OrderEntity toEntity(OrderRequestDto dto) {
        return OrderEntity.builder()
                .qty(dto.getQty())
                .menuId(dto.getMenuId())
                .customerId(dto.getCustomerId())
                .build();
    }

    public static OrderResponseDto toResponseDto(OrderEntity orderEntity, MenuDto menu) {
       return  OrderResponseDto.builder()
               .id(orderEntity.getId())
               .qty(orderEntity.getQty())
               .menu(menu)
               .build();
    }
}
