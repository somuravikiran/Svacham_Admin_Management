package com.svacham.Salary_Service.service.impl;

import com.svacham.Salary_Service.dto.SalarySummaryDto;
import com.svacham.Salary_Service.entity.Salary;
import com.svacham.Salary_Service.repository.SalaryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SalaryServiceImplTest {

    @Mock
    private SalaryRepository salaryRepository;

    @Mock
    private org.springframework.web.reactive.function.client.WebClient.Builder webClientBuilder;

    @InjectMocks
    private com.svacham.Salary_Service.service.impl.SalaryServiceImpl salaryService;

    @Test
    void addSalary_calculatesBalanceAndStatusAndSaves() {
        Salary s = new Salary();
        s.setMonthlySalary(50000.0);
        s.setPaidAmount(20000.0);

        when(salaryRepository.save(any(Salary.class))).thenAnswer(i -> i.getArgument(0));

        Salary saved = salaryService.addSalary(s);

        assertNotNull(saved.getCreatedAt());
        assertEquals(30000.0, saved.getBalanceAmount());
        assertEquals("PARTIAL", saved.getStatus());
        verify(salaryRepository, times(1)).save(any(Salary.class));
    }

    @Test
    void getAllSalaries_returnsList() {
        Salary s1 = new Salary(); s1.setEmployeeName("A");
        Salary s2 = new Salary(); s2.setEmployeeName("B");
        when(salaryRepository.findAll()).thenReturn(List.of(s1, s2));

        List<Salary> all = salaryService.getAllSalaries();

        assertEquals(2, all.size());
    }

    @Test
    void getSalaryById_found_returns() {
        Salary s = new Salary(); s.setEmployeeName("Emp");
        when(salaryRepository.findById("1")).thenReturn(Optional.of(s));

        Salary res = salaryService.getSalaryById("1");

        assertEquals("Emp", res.getEmployeeName());
    }

    @Test
    void getSalaryById_notFound_throws() {
        when(salaryRepository.findById("99")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> salaryService.getSalaryById("99"));
        assertTrue(ex.getMessage().contains("Salary not found"));
    }

    @Test
    void updateSalary_updatesFieldsAndSaves() {
        Salary existing = new Salary();
        existing.setEmployeeName("Old");
        existing.setMonthlySalary(30000.0);
        existing.setPaidAmount(10000.0);

        Salary update = new Salary();
        update.setEmployeeName("New");
        update.setMonthlySalary(40000.0);
        update.setPaidAmount(40000.0);
        update.setSalaryMonth("2026-05");

        when(salaryRepository.save(any(Salary.class))).thenAnswer(i -> i.getArgument(0));
        when(salaryRepository.findById("2")).thenReturn(Optional.of(existing));

        Salary res = salaryService.updateSalary("2", update);

        assertEquals("New", res.getEmployeeName());
        assertEquals(0.0, res.getBalanceAmount());
        assertEquals("PAID", res.getStatus());
    }

    @Test
    void deleteSalary_callsRepository() {
        doNothing().when(salaryRepository).deleteById("5");

        salaryService.deleteSalary("5");

        verify(salaryRepository, times(1)).deleteById("5");
    }

    @Test
    void getByStatus_callsRepository() {
        Salary s = new Salary(); s.setStatus("PENDING");
        when(salaryRepository.findByStatus("PENDING")).thenReturn(List.of(s));

        List<Salary> res = salaryService.getByStatus("PENDING");

        assertEquals(1, res.size());
    }

    @Test
    void getSalarySummary_computesAggregates() {
        Salary s1 = new Salary(); s1.setMonthlySalary(20000.0); s1.setPaidAmount(15000.0); s1.setBalanceAmount(5000.0);
        Salary s2 = new Salary(); s2.setMonthlySalary(30000.0); s2.setPaidAmount(20000.0); s2.setBalanceAmount(10000.0);

        when(salaryRepository.findAll()).thenReturn(List.of(s1, s2));

        SalarySummaryDto dto = salaryService.getSalarySummary();

        assertEquals(50000.0, dto.getTotalMonthlySalary());
        assertEquals(35000.0, dto.getTotalPaidSalary());
        assertEquals(15000.0, dto.getTotalPendingSalary());
        assertEquals(2, dto.getTotalEmployees());
    }
}


