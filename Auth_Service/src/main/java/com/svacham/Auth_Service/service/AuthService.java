package com.svacham.Auth_Service.service;


import com.svacham.Auth_Service.dto.AuthResponseDto;
import com.svacham.Auth_Service.dto.LoginRequestDto;
import com.svacham.Auth_Service.dto.RegisterRequestDto;

public interface AuthService {
    AuthResponseDto register(RegisterRequestDto dto);
    AuthResponseDto login(LoginRequestDto dto);
    Object validateToken(String token);
}