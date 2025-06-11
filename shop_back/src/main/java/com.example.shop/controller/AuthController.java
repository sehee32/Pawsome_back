package com.example.shop.controller;

import com.example.shop.dto.LoginRequestDto;
import com.example.shop.dto.LoginResponseDto;
import com.example.shop.dto.RegisterRequestDto;
import com.example.shop.service.AuthService;
import com.example.shop.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    public AuthController(AuthService authService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequest) {
        LoginResponseDto response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequestDto registerRequest) {
        authService.register(registerRequest);
        return ResponseEntity.ok("회원가입 성공!");
    }

    @GetMapping("/is-admin")
    public ResponseEntity<Boolean> isAdmin(@RequestHeader("Authorization") String token) {
        boolean isAdmin = authService.checkAdmin(token);
        return ResponseEntity.ok(isAdmin);
    }
}
