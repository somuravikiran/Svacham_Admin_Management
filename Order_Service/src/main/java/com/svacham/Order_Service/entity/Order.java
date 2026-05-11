package com.svacham.Order_Service.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "orders")
public class Order {

    @Id
    private String orderId;

    private String clientId;

    private String clientName;

    private LocalDate orderDate;

    private LocalDate deliveryDate;

    private Double totalAmount;

    private Double paidAmount;

    private Double pendingAmount;

    private String orderStatus;

    private List<OrderItem> items;
}