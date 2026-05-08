package com.svacham.DashBoard_Service.service.impl;

import com.svacham.DashBoard_Service.dto.AuthValidationResponseDto;
import com.svacham.DashBoard_Service.dto.ClientResponseDto;
import com.svacham.DashBoard_Service.dto.DashboardResponseDto;
import com.svacham.DashBoard_Service.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import org.springframework.http.HttpHeaders;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final RestTemplate restTemplate;

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
    // Get all clients from CLIENT-SERVICE
    public List<ClientResponseDto> getAllClients(String token) {

        return webClientBuilder.build()
                .get()
                .uri("http://CLIENT-SERVICE/api/clients/all")
                .header("Authorization", token)
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> response.bodyToMono(String.class)
                                .map(body -> new RuntimeException("CLIENT-SERVICE ERROR: " + body))
                )
                .bodyToFlux(ClientResponseDto.class)
                .collectList()
                .block();
    }

    // Get single client report
    public ClientResponseDto getClientReport(Long id,String token) {

        return webClientBuilder.build()
                .get()
                .uri("http://CLIENT-SERVICE/api/clients/" + id)
                .header("Authorization", token)
                .retrieve()
                .bodyToMono(ClientResponseDto.class)
                .block();
    }

    public Map getOrderReports(String token) {

        return webClientBuilder.build()
                .get()
                .uri("http://ORDER-SERVICE/api/orders/summary")
                .header("Authorization", token)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }
    public Map getClientReports(String token) {

        return webClientBuilder.build()
                .get()
                .uri("http://ORDER-SERVICE/api/clients/summary")
                .header("Authorization", token)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }

    @Override
    public DashboardResponseDto getAdminDashboardSummary(String token) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                "http://localhost:8082/api/clients/summary",
                HttpMethod.GET,
                entity,
                Map.class
        );

        Map client = getClientReports(token);
        Long totalClients = ((Number) client.get("totalClients")).longValue();
        Double totalClientPendingAmount = ((Number) client.get("totalPendingAmount")).doubleValue();

        Map order = getOrderReports(token);

        Long totalOrders = ((Number) order.get("totalOrders")).longValue();
        Double totalOrderRevenue = ((Number) order.get("totalRevenue")).doubleValue();
        Double totalOrderPendingRevenue = ((Number) order.get("pendingRevenue")).doubleValue();

        Map spending = restTemplate.getForObject("http://localhost:8087/api/spending/summary", Map.class);
        Map salary = restTemplate.getForObject("http://localhost:8086/api/salary/summary", Map.class);
        Map gst = restTemplate.getForObject("http://localhost:8084/api/gst-bill/summary", Map.class);
        Map stock = restTemplate.getForObject("http://localhost:8088/api/stock/summary", Map.class);

        Double totalSpendings = ((Number) spending.get("totalSpendings")).doubleValue();
        Double paidSpendings = ((Number) spending.get("paidAmount")).doubleValue();
        Double pendingSpendings = ((Number) spending.get("pendingAmount")).doubleValue();

        Double totalSalaryPaid = ((Number) salary.get("totalPaidSalary")).doubleValue();
        Double totalSalaryPending = ((Number) salary.get("totalPendingSalary")).doubleValue();

        Double totalGstPaid = ((Number) gst.get("totalPaidGst")).doubleValue();
        Double totalGstPending = ((Number) gst.get("totalPendingGst")).doubleValue();

        Double totalStockAvailable = ((Number) stock.get("totalStockAvailable")).doubleValue();
        Double totalInventoryValue = ((Number) stock.get("totalInventoryValue")).doubleValue();
        Long lowStockItems = ((Number) stock.get("lowStockItems")).longValue();
        Long outOfStockItems = ((Number) stock.get("outOfStockItems")).longValue();

        Double totalBusinessIncome = totalOrderRevenue;

        Double totalBusinessExpense = totalSpendings + totalSalaryPaid + totalGstPaid;

        Double netProfit = totalBusinessIncome - totalBusinessExpense;

        String businessStatus;

        if (netProfit > 50000) {
            businessStatus = "EXCELLENT";
        } else if (netProfit > 0) {
            businessStatus = "GOOD";
        } else {
            businessStatus = "LOSS";
        }

        return DashboardResponseDto.builder()
                .totalClients(totalClients)
                .totalClientPendingAmount(totalClientPendingAmount)
                .totalOrders(totalOrders)
                .totalOrderRevenue(totalOrderRevenue)
                .totalOrderPendingRevenue(totalOrderPendingRevenue)
                .totalSpendings(totalSpendings)
                .paidSpendings(paidSpendings)
                .pendingSpendings(pendingSpendings)
                .totalSalaryPaid(totalSalaryPaid)
                .totalSalaryPending(totalSalaryPending)
                .totalGstPaid(totalGstPaid)
                .totalGstPending(totalGstPending)
                .totalStockAvailable(totalStockAvailable)
                .totalInventoryValue(totalInventoryValue)
                .lowStockItems(lowStockItems)
                .outOfStockItems(outOfStockItems)
                .totalBusinessIncome(totalBusinessIncome)
                .totalBusinessExpense(totalBusinessExpense)
                .netProfit(netProfit)
                .businessStatus(businessStatus)
                .build();
    }

    @Override
    public DashboardResponseDto getAdminDashboardSummary() {
        // Delegate to token-based method without a token (no Authorization header)
        return getAdminDashboardSummary("");
    }
}