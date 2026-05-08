package com.svacham.Order_Service.dto;
import lombok.Data;

@Data
public class OrderItemDto {

    private String pickleType;
    private Double packSizeKg;
    private Integer quantity;
    private Double unitPrice;
}