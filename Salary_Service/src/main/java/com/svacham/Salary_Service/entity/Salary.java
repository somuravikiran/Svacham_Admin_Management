package com.svacham.Salary_Service.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Document(collection = "salary")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Salary {

    @Id
    private String id;

    private String employeeName;

    private String employeeRole;

    private Double monthlySalary;

    private Double paidAmount;

    private Double balanceAmount;

    private String salaryMonth;

    private LocalDate paymentDate;

    private String paymentMode;

    private String status;

    private String notes;

    private LocalDateTime createdAt;
}