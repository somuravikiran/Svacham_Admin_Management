package com.svacham.Spending_Service.service.impl;

import com.svacham.Spending_Service.dto.AuthValidationResponseDto;
import com.svacham.Spending_Service.entity.Spending;
import com.svacham.Spending_Service.repository.SpendingRepository;
import com.svacham.Spending_Service.service.SpendingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SpendingServiceImpl implements SpendingService {

    private final SpendingRepository spendingRepository;


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
    public Spending addSpending(Spending spending) {
        spending.setCreatedAt(LocalDateTime.now());
        return spendingRepository.save(spending);
    }

    @Override
    public List<Spending> getAllSpendings() {
        return spendingRepository.findAll();
    }

    @Override
    public Spending getSpendingById(Long id) {
        return spendingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Spending not found with id : " + id));
    }

    @Override
    public Spending updateSpending(Long id, Spending spending) {
        Spending existing = getSpendingById(id);

        existing.setExpenseTitle(spending.getExpenseTitle());
        existing.setExpenseCategory(spending.getExpenseCategory());
        existing.setDescription(spending.getDescription());
        existing.setAmount(spending.getAmount());
        existing.setSpentDate(spending.getSpentDate());
        existing.setVendorName(spending.getVendorName());
        existing.setPaymentMode(spending.getPaymentMode());
        existing.setStatus(spending.getStatus());

        return spendingRepository.save(existing);
    }

    @Override
    public void deleteSpending(Long id) {
        spendingRepository.deleteById(id);
    }

    @Override
    public List<Spending> getByStatus(String status) {
        return spendingRepository.findByStatus(status);
    }

    @Override
    public Map<String, Object> getSpendingSummary() {

        List<Spending> allSpendings = spendingRepository.findAll();

        double totalExpense = allSpendings.stream()
                .mapToDouble(Spending::getAmount)
                .sum();

        double paidExpense = allSpendings.stream()
                .filter(s -> "PAID".equalsIgnoreCase(s.getStatus()))
                .mapToDouble(Spending::getAmount)
                .sum();

        double pendingExpense = allSpendings.stream()
                .filter(s -> "PENDING".equalsIgnoreCase(s.getStatus()))
                .mapToDouble(Spending::getAmount)
                .sum();

        double cancelledExpense = allSpendings.stream()
                .filter(s -> "CANCELLED".equalsIgnoreCase(s.getStatus()))
                .mapToDouble(Spending::getAmount)
                .sum();

        Map<String, Object> summary = new HashMap<>();
        summary.put("totalExpense", totalExpense);
        summary.put("paidExpense", paidExpense);
        summary.put("pendingExpense", pendingExpense);
        summary.put("cancelledExpense", cancelledExpense);
        summary.put("totalTransactions", allSpendings.size());

        return summary;
    }
}