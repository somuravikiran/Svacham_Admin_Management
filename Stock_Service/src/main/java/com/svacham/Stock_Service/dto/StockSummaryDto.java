package com.svacham.Stock_Service.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockSummaryDto {

    private Double totalStockAvailable;
    private Double totalUsedStock;
    private Double totalInventoryValue;
    private Long lowStockItems;
    private Long outOfStockItems;
}
