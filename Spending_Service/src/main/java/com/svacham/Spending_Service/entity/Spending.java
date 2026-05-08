package com.svacham.Spending_Service.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "spending")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Spending {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String expenseTitle;

    private String expenseCategory;

    private String description;

    private Double amount;

    private LocalDate spentDate;

    private String vendorName;

    private String paymentMode;

    private String status;   // PAID / PENDING / CANCELLED

    private LocalDateTime createdAt;
}