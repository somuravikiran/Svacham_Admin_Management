package com.svacham.Salary_Service.service.impl;
import com.svacham.Salary_Service.dto.AuthValidationResponseDto;
import com.svacham.Salary_Service.dto.SalarySummaryDto;
import com.svacham.Salary_Service.entity.Salary;
import com.svacham.Salary_Service.repository.SalaryRepository;
import com.svacham.Salary_Service.service.SalaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SalaryServiceImpl implements SalaryService {

    private final SalaryRepository salaryRepository;


    private final WebClient.Builder webClientBuilder;

    public AuthValidationResponseDto validateToken(String token) {
        try {
//            System.out.println("STEP 3 : CALLING AUTH-SERVICE");

            AuthValidationResponseDto response = webClientBuilder.build()
                    .get()
                    .uri("http://AUTH-SERVICE/auth/validate")
                    .header("Authorization", token)
                    .retrieve()
                    .bodyToMono(AuthValidationResponseDto.class)
                    .block();

//            System.out.println("STEP 4 : AUTH RESPONSE = " + response);
            return response;
        } catch (Exception e) {
            throw new RuntimeException("AUTH-SERVICE is unavailable : " + e.getMessage());
        }
    }

    @Override
    public Salary addSalary(Salary salary) {

        double balance = salary.getMonthlySalary() - salary.getPaidAmount();
        salary.setBalanceAmount(balance);

        if(balance == 0){
            salary.setStatus("PAID");
        } else if (salary.getPaidAmount() > 0) {
            salary.setStatus("PARTIAL");
        } else {
            salary.setStatus("PENDING");
        }

        salary.setCreatedAt(LocalDateTime.now());

        return salaryRepository.save(salary);
    }

    @Override
    public List<Salary> getAllSalaries() {
        return salaryRepository.findAll();
    }

    @Override
    public Salary getSalaryById(Long id) {
        return salaryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Salary not found with id : " + id));
    }

    @Override
    public Salary updateSalary(Long id, Salary salary) {

        Salary existing = getSalaryById(id);

        existing.setEmployeeName(salary.getEmployeeName());
        existing.setEmployeeRole(salary.getEmployeeRole());
        existing.setMonthlySalary(salary.getMonthlySalary());
        existing.setPaidAmount(salary.getPaidAmount());

        double balance = salary.getMonthlySalary() - salary.getPaidAmount();
        existing.setBalanceAmount(balance);

        if(balance == 0){
            existing.setStatus("PAID");
        } else if (salary.getPaidAmount() > 0) {
            existing.setStatus("PARTIAL");
        } else {
            existing.setStatus("PENDING");
        }

        existing.setSalaryMonth(salary.getSalaryMonth());
        existing.setPaymentDate(salary.getPaymentDate());
        existing.setPaymentMode(salary.getPaymentMode());
        existing.setNotes(salary.getNotes());

        return salaryRepository.save(existing);
    }

    @Override
    public void deleteSalary(Long id) {
        salaryRepository.deleteById(id);
    }

    @Override
    public List<Salary> getByStatus(String status) {
        return salaryRepository.findByStatus(status);
    }

    @Override
    public SalarySummaryDto getSalarySummary() {

        List<Salary> all = salaryRepository.findAll();

        double totalMonthlySalary = all.stream()
                .mapToDouble(Salary::getMonthlySalary)
                .sum();

        double totalPaidSalary = all.stream()
                .mapToDouble(Salary::getPaidAmount)
                .sum();

        double totalPendingSalary = all.stream()
                .mapToDouble(Salary::getBalanceAmount)
                .sum();

        return SalarySummaryDto.builder()
                .totalMonthlySalary(totalMonthlySalary)
                .totalPaidSalary(totalPaidSalary)
                .totalPendingSalary(totalPendingSalary)
                .totalEmployees(all.size())
                .build();
    }
}