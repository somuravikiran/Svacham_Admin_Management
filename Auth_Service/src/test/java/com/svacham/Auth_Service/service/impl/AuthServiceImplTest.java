package com.svacham.Auth_Service.service.impl;

import com.svacham.Auth_Service.config.JwtService;
import com.svacham.Auth_Service.dto.AuthResponseDto;
import com.svacham.Auth_Service.dto.LoginRequestDto;
import com.svacham.Auth_Service.dto.RegisterRequestDto;
import com.svacham.Auth_Service.emtity.User;
import com.svacham.Auth_Service.exception.DuplicateResourceException;
import com.svacham.Auth_Service.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        // Mockito will initialize mocks
    }

    @Test
    void register_success_returnsTokenAndMessage() {
        RegisterRequestDto dto = new RegisterRequestDto();
        dto.setFullName("Test User");
        dto.setEmail("test@example.com");
        dto.setPassword("plainpwd");

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(dto.getPassword())).thenReturn("encodedPwd");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));
        when(jwtService.generateToken(dto.getEmail())).thenReturn("jwt-token-123");

        AuthResponseDto resp = authService.register(dto);

        assertNotNull(resp);
        assertEquals("jwt-token-123", resp.getToken());
        assertEquals("User Registered Successfully", resp.getMessage());
    }

    @Test
    void register_duplicateEmail_throwsDuplicateResourceException() {
        RegisterRequestDto dto = new RegisterRequestDto();
        dto.setFullName("Test User");
        dto.setEmail("exists@example.com");
        dto.setPassword("pwd");

        User existing = User.builder().email(dto.getEmail()).fullName("Existing").password("encoded").build();
        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(existing));

        assertThrows(DuplicateResourceException.class, () -> authService.register(dto));
    }

    @Test
    void login_success_returnsToken() {
        LoginRequestDto dto = new LoginRequestDto();
        dto.setEmail("user@example.com");
        dto.setPassword("plainpwd");

        User user = User.builder()
                .email(dto.getEmail())
                .password("encodedPwd")
                .fullName("User")
                .build();

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(dto.getPassword(), user.getPassword())).thenReturn(true);
        when(jwtService.generateToken(dto.getEmail())).thenReturn("login-token-xyz");

        AuthResponseDto resp = authService.login(dto);

        assertNotNull(resp);
        assertEquals("login-token-xyz", resp.getToken());
        assertEquals("Login Success", resp.getMessage());
    }

    @Test
    void login_invalidEmail_throwsRuntimeException() {
        LoginRequestDto dto = new LoginRequestDto();
        dto.setEmail("noone@example.com");
        dto.setPassword("pwd");

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> authService.login(dto));
        assertEquals("Invalid Email", ex.getMessage());
    }

    @Test
    void login_invalidPassword_throwsRuntimeException() {
        LoginRequestDto dto = new LoginRequestDto();
        dto.setEmail("user2@example.com");
        dto.setPassword("wrongpwd");

        User user = User.builder().email(dto.getEmail()).password("encodedPwd").build();
        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(dto.getPassword(), user.getPassword())).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> authService.login(dto));
        assertEquals("Invalid Password", ex.getMessage());
    }
}

