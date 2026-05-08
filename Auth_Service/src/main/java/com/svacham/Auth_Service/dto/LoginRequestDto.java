package com.svacham.Auth_Service.dto;

import lombok.Data;

@Data
public class LoginRequestDto {
    private String email;
    private String password;
}