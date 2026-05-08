package com.svacham.Salary_Service.service;


import com.svacham.Salary_Service.dto.AuthValidationResponseDto;
import com.svacham.Salary_Service.dto.SalarySummaryDto;
import com.svacham.Salary_Service.entity.Salary;

import java.util.List;

public interface SalaryService {

    AuthValidationResponseDto validateToken(String token);

    Salary addSalary(Salary salary);

    List<Salary> getAllSalaries();

    Salary getSalaryById(Long id);

    Salary updateSalary(Long id, Salary salary);

    void deleteSalary(Long id);

    List<Salary> getByStatus(String status);

    SalarySummaryDto getSalarySummary();
}