package com.svacham.Spending_Service.service;

import com.svacham.Spending_Service.dto.AuthValidationResponseDto;
import com.svacham.Spending_Service.dto.SpendingSummaryDto;
import com.svacham.Spending_Service.entity.Spending;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface SpendingService {
    AuthValidationResponseDto validateToken(String token);

    Spending addSpending(Spending spending);

    List<Spending> getAllSpendings();

    Spending getSpendingById(Long id);

    Spending updateSpending(Long id, Spending spending);

    void deleteSpending(Long id);

    List<Spending> getByStatus(String status);

    Map<String, Object> getSpendingSummary();
}