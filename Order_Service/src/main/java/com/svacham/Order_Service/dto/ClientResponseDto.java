package com.svacham.Order_Service.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ClientResponseDto {

    private String id;
    private String clientName;
    private String phoneNumber;
    private String address;
    private String city;
    private String state;
    private Double totalAmount;
    private Double amountPaid;
    private Double balanceAmount;
    private LocalDate createdDate;
}