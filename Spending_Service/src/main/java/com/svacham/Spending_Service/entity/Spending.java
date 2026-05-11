package com.svacham.Spending_Service.entity;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Document(collection = "spending")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Spending {

    @Id
    private String id;

    private String expenseTitle;

    private String expenseCategory;

    private String description;

    private Double amount;

    private LocalDate spentDate;

    private String vendorName;

    private String paymentMode;

    private String status;

    private LocalDateTime createdAt;
}