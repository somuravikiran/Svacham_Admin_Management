package com.svacham.Order_Service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StateOrderSummaryDto {

    private Double totalRevenue;
    private Double totalPending;
    private Long totalOrders;
}