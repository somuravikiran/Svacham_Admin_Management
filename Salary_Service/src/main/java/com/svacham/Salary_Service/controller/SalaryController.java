package com.svacham.Salary_Service.controller;

import com.svacham.Salary_Service.dto.SalarySummaryDto;
import com.svacham.Salary_Service.entity.Salary;
import com.svacham.Salary_Service.service.SalaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static io.netty.handler.codec.http.HttpHeaderValidationUtil.validateToken;

@RestController
@RequestMapping("/api/salary")
@RequiredArgsConstructor
public class SalaryController {


    private final SalaryService salaryService;

    @PostMapping("/add")
    public Salary addSalary(@RequestHeader("Authorization") String token,@RequestBody Salary salary) {
        validateToken(token);
        return salaryService.addSalary(salary);
    }

    @GetMapping("/all")
    public List<Salary> getAllSalaries(@RequestHeader("Authorization") String token) {
        validateToken(token);
        return salaryService.getAllSalaries();
    }

    @GetMapping("/{id}")
    public Salary getSalaryById(@RequestHeader("Authorization") String token,@PathVariable Long id) {
        validateToken(token);
        return salaryService.getSalaryById(id);
    }

    @PutMapping("/update/{id}")
    public Salary updateSalary(@RequestHeader("Authorization") String token,@PathVariable Long id, @RequestBody Salary salary) {
        validateToken(token);
        return salaryService.updateSalary(id, salary);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteSalary(@RequestHeader("Authorization") String token,@PathVariable Long id) {
        validateToken(token);
        salaryService.deleteSalary(id);
        return "Salary deleted successfully";
    }

    @GetMapping("/status/{status}")
    public List<Salary> getByStatus(@RequestHeader("Authorization") String token,@PathVariable String status) {
        validateToken(token);
        return salaryService.getByStatus(status);
    }

    @GetMapping("/summary")
    public SalarySummaryDto getSalarySummary(@RequestHeader("Authorization") String token) {
        return salaryService.getSalarySummary();
    }
}