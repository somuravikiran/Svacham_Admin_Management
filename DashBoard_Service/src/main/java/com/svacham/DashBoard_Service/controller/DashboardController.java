package com.svacham.DashBoard_Service.controller;

import com.svacham.DashBoard_Service.dto.ClientResponseDto;
import com.svacham.DashBoard_Service.dto.DashboardResponseDto;
import com.svacham.DashBoard_Service.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    private void validateToken(String token) {
        var response = dashboardService.validateToken(token);

        if (response == null || !response.isValid()) {
            throw new RuntimeException("Unauthorized Access - Invalid Token");
        }
    }
    // All clients report
    @GetMapping("/clients")
    public List<ClientResponseDto> getAllClients(@RequestHeader("Authorization") String token) {
        dashboardService.validateToken(token);
        return dashboardService.getAllClients(token);
    }

    // Single client report
    @GetMapping("/client/{id}")
    public ClientResponseDto getClient(@RequestHeader("Authorization") String token,@PathVariable Long id) {
        dashboardService.validateToken(token);
        return dashboardService.getClientReport(id,token);
    }

    @GetMapping("/admin-summary")
    public DashboardResponseDto getAdminDashboardSummary(
            @RequestHeader("Authorization") String token) {

        dashboardService.validateToken(token);
        return dashboardService.getAdminDashboardSummary(token);
    }
}