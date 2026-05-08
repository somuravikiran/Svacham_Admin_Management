package com.svacham.Client_Service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthValidationResponseDto {
    private boolean valid;
    private String email;
    private String fullName;
    private String role;
    private String message;
}