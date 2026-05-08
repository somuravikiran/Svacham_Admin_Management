package com.svacham.Salary_Service.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "salary")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Salary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String employeeName;

    private String employeeRole;

    private Double monthlySalary;

    private Double paidAmount;

    private Double balanceAmount;

    private String salaryMonth;

    private LocalDate paymentDate;

    private String paymentMode;

    private String status;   // PAID / PARTIAL / PENDING

    private String notes;

    private LocalDateTime createdAt;
}