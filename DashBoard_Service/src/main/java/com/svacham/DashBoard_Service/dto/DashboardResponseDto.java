package com.svacham.DashBoard_Service.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardResponseDto {

    private Long totalClients;
    private Double totalClientPendingAmount;

    private Long totalOrders;
    private Double totalOrderRevenue;
    private Double totalOrderPendingRevenue;

    private Double totalSpendings;
    private Double paidSpendings;
    private Double pendingSpendings;

    private Double totalSalaryPaid;
    private Double totalSalaryPending;

    private Double totalGstPaid;
    private Double totalGstPending;

    private Double totalStockAvailable;
    private Double totalInventoryValue;
    private Long lowStockItems;
    private Long outOfStockItems;

    private Double totalBusinessIncome;
    private Double totalBusinessExpense;
    private Double netProfit;

    private String businessStatus;
}