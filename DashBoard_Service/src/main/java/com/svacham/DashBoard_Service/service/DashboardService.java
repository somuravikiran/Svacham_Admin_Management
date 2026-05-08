package com.svacham.DashBoard_Service.service;


import com.svacham.DashBoard_Service.dto.AuthValidationResponseDto;
import com.svacham.DashBoard_Service.dto.ClientResponseDto;
import com.svacham.DashBoard_Service.dto.DashboardResponseDto;

import java.util.List;

public interface DashboardService {

    List<ClientResponseDto> getAllClients(String token);

    ClientResponseDto getClientReport(Long id,String token);

    AuthValidationResponseDto validateToken(String token);

    // Overloads: token-aware and token-less variants
    DashboardResponseDto getAdminDashboardSummary();

    DashboardResponseDto getAdminDashboardSummary(String token);
}