package com.svacham.DashBoard_Service.service.impl;

import com.svacham.DashBoard_Service.dto.DashboardResponseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DashboardServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private DashboardServiceImpl dashboardService;

    @Test
    void getAdminDashboardSummary_computesCorrectly() {
        // prepare mock responses
        Map<String, Object> client = Map.of(
                "totalClients", 10,
                "totalPendingAmount", 5000.0
        );

        Map<String, Object> order = Map.of(
                "totalOrders", 20,
                "totalRevenue", 200000.0,
                "pendingRevenue", 10000.0
        );

        Map<String, Object> spending = Map.of(
                "totalSpendings", 30000.0,
                "paidAmount", 20000.0,
                "pendingAmount", 10000.0
        );

        Map<String, Object> salary = Map.of(
                "totalPaidSalary", 40000.0,
                "totalPendingSalary", 5000.0
        );

        Map<String, Object> gst = Map.of(
                "totalPaidGst", 15000.0,
                "totalPendingGst", 2000.0
        );

        Map<String, Object> stock = Map.of(
                "totalStockAvailable", 100.0,
                "totalInventoryValue", 50000.0,
                "lowStockItems", 5,
                "outOfStockItems", 2
        );

        when(restTemplate.getForObject("http://localhost:8082/api/client/summary", Map.class)).thenReturn(client);
        when(restTemplate.getForObject("http://localhost:8085/api/order/summary", Map.class)).thenReturn(order);
        when(restTemplate.getForObject("http://localhost:8087/api/spending/summary", Map.class)).thenReturn(spending);
        when(restTemplate.getForObject("http://localhost:8086/api/salary/summary", Map.class)).thenReturn(salary);
        when(restTemplate.getForObject("http://localhost:8084/api/gst-bill/summary", Map.class)).thenReturn(gst);
        when(restTemplate.getForObject("http://localhost:8088/api/stock/summary", Map.class)).thenReturn(stock);

        DashboardResponseDto dto = dashboardService.getAdminDashboardSummary();

        assertNotNull(dto);
        assertEquals(10L, dto.getTotalClients());
        assertEquals(5000.0, dto.getTotalClientPendingAmount());

        assertEquals(20L, dto.getTotalOrders());
        assertEquals(200000.0, dto.getTotalOrderRevenue());
        assertEquals(10000.0, dto.getTotalOrderPendingRevenue());

        assertEquals(30000.0, dto.getTotalSpendings());
        assertEquals(20000.0, dto.getPaidSpendings());
        assertEquals(10000.0, dto.getPendingSpendings());

        assertEquals(40000.0, dto.getTotalSalaryPaid());
        assertEquals(5000.0, dto.getTotalSalaryPending());

        assertEquals(15000.0, dto.getTotalGstPaid());
        assertEquals(2000.0, dto.getTotalGstPending());

        assertEquals(100.0, dto.getTotalStockAvailable());
        assertEquals(50000.0, dto.getTotalInventoryValue());
        assertEquals(5L, dto.getLowStockItems());
        assertEquals(2L, dto.getOutOfStockItems());

        // business calculations
        assertEquals(200000.0, dto.getTotalBusinessIncome());
        assertEquals(30000.0 + 40000.0 + 15000.0, dto.getTotalBusinessExpense());
        assertEquals(200000.0 - (30000.0 + 40000.0 + 15000.0), dto.getNetProfit());
        assertEquals("EXCELLENT", dto.getBusinessStatus());
    }
}

