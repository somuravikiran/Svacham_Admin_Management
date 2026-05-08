package com.svacham.Auth_Service.controller;
import com.svacham.Auth_Service.dto.AuthResponseDto;
import com.svacham.Auth_Service.dto.LoginRequestDto;
import com.svacham.Auth_Service.dto.RegisterRequestDto;
import com.svacham.Auth_Service.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/validate")
    public Object validateToken(@RequestHeader("Authorization") String token) {
        System.out.println("VALIDATE API HIT");
        return authService.validateToken(token);
    }

    @PostMapping("/register")
    public AuthResponseDto register(@RequestBody RegisterRequestDto dto){
        return authService.register(dto);
    }

    @PostMapping("/login")
    public AuthResponseDto login(@RequestBody LoginRequestDto dto){
        return authService.login(dto);
    }
}