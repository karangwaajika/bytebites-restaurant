package com.lab.order_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuResponseDto {
    private Long customerId;
    private Long menuId;
    private int qty;
    private MenuDto menu;
}
