package com.svacham.Client_Service.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientSummaryDto {

    private Long totalClients;
    private Double totalBusinessAmount;
    private Double totalAmountPaid;
    private Double totalPendingBalance;
}