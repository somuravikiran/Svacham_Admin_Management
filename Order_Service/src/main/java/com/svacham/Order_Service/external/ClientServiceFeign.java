package com.svacham.Order_Service.external;

import com.svacham.Order_Service.dto.ClientResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "CLIENT-SERVICE")
public interface ClientServiceFeign {

    @GetMapping("/api/clients/{id}")
    ClientResponseDto getClientById(@RequestHeader("Authorization") String token, @PathVariable("id") String id);
}