package com.svacham.GstBill_Service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GstBillSummaryDto {

    private Long totalBills;

    private Double totalBillAmount;

    private Double totalGstAmount;

    private Double totalFinalAmount;

    private Long paidBills;

    private Long pendingBills;

    private Long partialBills;
}