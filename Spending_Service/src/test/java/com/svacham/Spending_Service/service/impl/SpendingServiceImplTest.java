package com.svacham.Spending_Service.service.impl;

import com.svacham.Spending_Service.entity.Spending;
import com.svacham.Spending_Service.repository.SpendingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpendingServiceImplTest {

    @Mock
    private SpendingRepository spendingRepository;

    @InjectMocks
    private com.svacham.Spending_Service.service.impl.SpendingServiceImpl spendingService;

    @Test
    void addSpending_setsCreatedAtAndSaves() {
        Spending s = new Spending();
        s.setExpenseTitle("Office Supplies");
        s.setAmount(1500.0);

        when(spendingRepository.save(any(Spending.class))).thenAnswer(i -> i.getArgument(0));

        Spending saved = spendingService.addSpending(s);

        assertNotNull(saved.getCreatedAt());
        assertEquals("Office Supplies", saved.getExpenseTitle());
        verify(spendingRepository, times(1)).save(any(Spending.class));
    }

    @Test
    void getAllSpendings_returnsList() {
        Spending s1 = new Spending(); s1.setExpenseTitle("A");
        Spending s2 = new Spending(); s2.setExpenseTitle("B");
        when(spendingRepository.findAll()).thenReturn(List.of(s1, s2));

        List<Spending> all = spendingService.getAllSpendings();

        assertEquals(2, all.size());
    }

    @Test
    void getSpendingById_found_returns() {
        Spending s = new Spending(); s.setExpenseTitle("Found");
        when(spendingRepository.findById("1")).thenReturn(Optional.of(s));

        Spending res = spendingService.getSpendingById("1");

        assertEquals("Found", res.getExpenseTitle());
    }

    @Test
    void getSpendingById_notFound_throws() {
        when(spendingRepository.findById("99")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> spendingService.getSpendingById("99"));
        assertTrue(ex.getMessage().contains("Spending not found"));
    }

    @Test
    void updateSpending_updatesFieldsAndSaves() {
        Spending existing = new Spending();
        existing.setExpenseTitle("Old");
        existing.setAmount(1000.0);

        Spending update = new Spending();
        update.setExpenseTitle("New");
        update.setAmount(2000.0);
        update.setStatus("PAID");

        when(spendingRepository.findById("2")).thenReturn(Optional.of(existing));
        when(spendingRepository.save(any(Spending.class))).thenAnswer(i -> i.getArgument(0));

        Spending res = spendingService.updateSpending("2", update);

        assertEquals("New", res.getExpenseTitle());
        assertEquals(2000.0, res.getAmount());
        assertEquals("PAID", res.getStatus());
    }

    @Test
    void deleteSpending_callsRepository() {
        doNothing().when(spendingRepository).deleteById("5");

        spendingService.deleteSpending("5");

        verify(spendingRepository, times(1)).deleteById("5");
    }

    @Test
    void getByStatus_callsRepository() {
        Spending s = new Spending(); s.setStatus("PENDING");
        when(spendingRepository.findByStatus("PENDING")).thenReturn(List.of(s));

        List<Spending> res = spendingService.getByStatus("PENDING");

        assertEquals(1, res.size());
    }

    @Test
    void getSpendingSummary_computesAggregates() {
        Spending p = new Spending(); p.setAmount(1000.0); p.setStatus("PAID");
        Spending pend = new Spending(); pend.setAmount(500.0); pend.setStatus("PENDING");
        Spending can = new Spending(); can.setAmount(200.0); can.setStatus("CANCELLED");

        when(spendingRepository.findAll()).thenReturn(List.of(p, pend, can));

        Map<String, Object> summary = spendingService.getSpendingSummary();

        assertEquals(1700.0, (double) summary.get("totalExpense"));
        assertEquals(1000.0, (double) summary.get("paidExpense"));
        assertEquals(500.0, (double) summary.get("pendingExpense"));
        assertEquals(200.0, (double) summary.get("cancelledExpense"));
        assertEquals(3, summary.get("totalTransactions"));
    }
}


