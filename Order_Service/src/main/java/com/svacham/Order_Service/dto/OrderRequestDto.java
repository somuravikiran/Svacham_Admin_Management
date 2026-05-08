package com.svacham.Order_Service.dto;


import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class OrderRequestDto {

    private Long clientId;
    private String clientName;
    private LocalDate orderDate;
    private LocalDate deliveryDate;
    private Double paidAmount;
    private List<OrderItemDto> items;
}