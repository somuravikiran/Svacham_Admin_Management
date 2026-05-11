package com.svacham.GstBill_Service.service.impl;
import com.svacham.GstBill_Service.dto.AuthValidationResponseDto;
import com.svacham.GstBill_Service.dto.GstBillSummaryDto;
import com.svacham.GstBill_Service.entity.GstBill;
import com.svacham.GstBill_Service.repository.GstBillRepository;
import com.svacham.GstBill_Service.service.GstBillService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.List;
@Service
@RequiredArgsConstructor
public class GstBillServiceImpl implements GstBillService {

    private final GstBillRepository gstBillRepository;

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
    public GstBill createBill(GstBill gstBill) {
        gstBill.setCreatedAt(LocalDateTime.now());
        return gstBillRepository.save(gstBill);
    }

    @Override
    public GstBill updateBill(String id, GstBill gstBill) {
        GstBill existing = gstBillRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("GST Bill not found with id : " + id));

        existing.setVendorName(gstBill.getVendorName());
        existing.setBillNumber(gstBill.getBillNumber());
        existing.setItemPurchased(gstBill.getItemPurchased());
        existing.setBillAmount(gstBill.getBillAmount());
        existing.setGstPercent(gstBill.getGstPercent());
        existing.setGstAmount(gstBill.getGstAmount());
        existing.setTotalAmount(gstBill.getTotalAmount());
        existing.setBillDate(gstBill.getBillDate());
        existing.setPaymentMode(gstBill.getPaymentMode());
        existing.setStatus(gstBill.getStatus());
        existing.setNotes(gstBill.getNotes());

        return gstBillRepository.save(existing);
    }

    @Override
    public GstBill getBillById(String id) {
        return gstBillRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("GST Bill not found with id : " + id));
    }

    @Override
    public List<GstBill> getAllBills() {
        return gstBillRepository.findAll();
    }

    @Override
    public void deleteBill(String id) {
        gstBillRepository.deleteById(id);
    }

    @Override
    public GstBillSummaryDto getSummary() {

        List<GstBill> bills = gstBillRepository.findAll();

        double totalBillAmount = bills.stream()
                .mapToDouble(b ->
                        b.getBillAmount() != null
                                ? b.getBillAmount()
                                : 0
                )
                .sum();

        double totalGstAmount = bills.stream()
                .mapToDouble(b ->
                        b.getGstAmount() != null
                                ? b.getGstAmount()
                                : 0
                )
                .sum();

        double totalFinalAmount = bills.stream()
                .mapToDouble(b ->
                        b.getTotalAmount() != null
                                ? b.getTotalAmount()
                                : 0
                )
                .sum();

        long paidBills = bills.stream()
                .filter(b ->
                        "PAID".equalsIgnoreCase(b.getStatus())
                )
                .count();

        long pendingBills = bills.stream()
                .filter(b ->
                        "PENDING".equalsIgnoreCase(b.getStatus())
                )
                .count();

        long partialBills = bills.stream()
                .filter(b ->
                        "PARTIAL".equalsIgnoreCase(b.getStatus())
                )
                .count();

        return GstBillSummaryDto.builder()
                .totalBills((long) bills.size())
                .totalBillAmount(totalBillAmount)
                .totalGstAmount(totalGstAmount)
                .totalFinalAmount(totalFinalAmount)
                .paidBills(paidBills)
                .pendingBills(pendingBills)
                .partialBills(partialBills)
                .build();
    }
}