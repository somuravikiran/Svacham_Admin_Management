package com.svacham.Salary_Service.controller;

import com.svacham.Salary_Service.dto.SalarySummaryDto;
import com.svacham.Salary_Service.entity.Salary;
import com.svacham.Salary_Service.service.SalaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/salary")
@RequiredArgsConstructor
public class SalaryController {

    private final SalaryService salaryService;

    @PostMapping("/add")
    public Salary addSalary(@RequestHeader("Authorization") String token,
                            @RequestBody Salary salary) {

        salaryService.validateToken(token);

        return salaryService.addSalary(salary);
    }

    @GetMapping("/all")
    public List<Salary> getAllSalaries(@RequestHeader("Authorization") String token) {

        salaryService.validateToken(token);

        return salaryService.getAllSalaries();
    }

    @GetMapping("/{id}")
    public Salary getSalaryById(@RequestHeader("Authorization") String token,
                                @PathVariable String id) {

        salaryService.validateToken(token);

        return salaryService.getSalaryById(id);
    }

    @PutMapping("/update/{id}")
    public Salary updateSalary(@RequestHeader("Authorization") String token,
                               @PathVariable String id,
                               @RequestBody Salary salary) {

        salaryService.validateToken(token);

        return salaryService.updateSalary(id, salary);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteSalary(@RequestHeader("Authorization") String token,
                               @PathVariable String id) {

        salaryService.validateToken(token);

        salaryService.deleteSalary(id);

        return "Salary deleted successfully";
    }

    @GetMapping("/status/{status}")
    public List<Salary> getByStatus(@RequestHeader("Authorization") String token,
                                    @PathVariable String status) {

        salaryService.validateToken(token);

        return salaryService.getByStatus(status);
    }

    @GetMapping("/summary")
    public SalarySummaryDto getSalarySummary(@RequestHeader("Authorization") String token) {

        salaryService.validateToken(token);

        return salaryService.getSalarySummary();
    }
}