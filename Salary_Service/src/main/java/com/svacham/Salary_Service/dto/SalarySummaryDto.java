package com.svacham.Salary_Service.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalarySummaryDto {

    private double totalMonthlySalary;

    private double totalPaidSalary;

    private double totalPendingSalary;

    private long totalEmployees;
}