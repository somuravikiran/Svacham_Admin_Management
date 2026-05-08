package com.svacham.Auth_Service.service.impl;
import com.svacham.Auth_Service.config.JwtService;
import com.svacham.Auth_Service.dto.AuthResponseDto;
import com.svacham.Auth_Service.dto.AuthValidationResponseDto;
import com.svacham.Auth_Service.dto.LoginRequestDto;
import com.svacham.Auth_Service.dto.RegisterRequestDto;
import com.svacham.Auth_Service.emtity.User;
import com.svacham.Auth_Service.repository.UserRepository;
import com.svacham.Auth_Service.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.svacham.Auth_Service.exception.DuplicateResourceException;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public AuthResponseDto register(RegisterRequestDto dto) {

        // check for existing user and throw a specific exception for duplicate email
        userRepository.findByEmail(dto.getEmail()).ifPresent(u -> {
            throw new DuplicateResourceException("Email already in use: " + dto.getEmail());
        });

        User user = User.builder()
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role("ADMIN")
                .build();

        userRepository.save(user);

        String token = jwtService.generateToken(user.getEmail());

        return new AuthResponseDto(token,"User Registered Successfully");
    }

    @Override
    public AuthResponseDto login(LoginRequestDto dto) {

        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid Email"));

        if(!passwordEncoder.matches(dto.getPassword(), user.getPassword())){
            throw new RuntimeException("Invalid Password");
        }

        String token = jwtService.generateToken(user.getEmail());

        return new AuthResponseDto(token,"Login Success");
    }
    @Override
    public Object validateToken(String token) {

        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        String email = jwtService.extractUsername(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        boolean valid = jwtService.isTokenValid(token, email);

        return new AuthValidationResponseDto(
                valid,
                user.getEmail(),
                user.getFullName(),
                user.getRole(),
                valid ? "VALID TOKEN" : "INVALID TOKEN"
        );
    }
}