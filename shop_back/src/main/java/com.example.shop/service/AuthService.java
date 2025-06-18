package com.example.shop.service;

import com.example.shop.dto.LoginRequestDto;
import com.example.shop.dto.LoginResponseDto;
import com.example.shop.dto.RegisterRequestDto;
import com.example.shop.entity.User;
import com.example.shop.exception.AuthenticationException;
import com.example.shop.repository.UserRepository;
import org.springframework.stereotype.Service;
import com.example.shop.util.JwtUtil;


@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    public boolean isEmailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public void register(RegisterRequestDto registerRequest) {
        // 회원가입 시 중복 방지
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new RuntimeException("이미 사용 중인 이메일입니다.");
        }
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(registerRequest.getPassword()); // 평문 비밀번호 저장 (암호화 제거)
        userRepository.save(user);
    }

    public LoginResponseDto login(LoginRequestDto loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new AuthenticationException("사용자를 찾을 수 없습니다."));

        if (!loginRequest.getPassword().equals(user.getPassword())) { // 평문 비교
            throw new AuthenticationException("비밀번호가 일치하지 않습니다.");
        }

        boolean isAdmin = "admin@admin.com".equals(user.getEmail()); // 관리자 계정

        String token = jwtUtil.generateToken(user.getUsername(), user.getId(), isAdmin); // JWT 생성

        return new LoginResponseDto(token, user.getUsername(), isAdmin);
    }

    public boolean checkAdmin(String token) {
        try {
            String jwtToken = token.replace("Bearer ", "");
            return jwtUtil.extractIsAdmin(jwtToken); // JWT 유틸리티를 사용해 관리자 여부 확인
        } catch (Exception e) {
            return false; // 예외 발생 시 기본값 반환
        }
    }
}
