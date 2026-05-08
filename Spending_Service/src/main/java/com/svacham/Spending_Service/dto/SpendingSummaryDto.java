package com.svacham.Spending_Service.dto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpendingSummaryDto {
    private double totalExpense;

    private double paidExpense;

    private double pendingExpense;

    private double cancelledExpense;

    private long totalTransactions;
}