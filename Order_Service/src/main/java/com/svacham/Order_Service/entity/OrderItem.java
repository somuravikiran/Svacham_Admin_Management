package com.svacham.Order_Service.entity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {

    private String itemId;

    private String pickleType;

    private Double packSizeKg;

    private Integer quantity;

    private Double unitPrice;

    private Double subTotal;
}