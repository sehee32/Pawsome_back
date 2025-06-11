package com.example.shop.controller;

import com.example.shop.dto.ChangePasswordDto;
import com.example.shop.dto.UserInfoDto;
import com.example.shop.dto.OrderDto;
import com.example.shop.entity.User;
import com.example.shop.repository.UserRepository;
import com.example.shop.service.OrderService;
import com.example.shop.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final OrderService orderService;

    private Long getUserId(String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            return jwtUtil.extractUserId(token);
        } catch (Exception e) {
            throw new RuntimeException("Invalid token");
        }
    }

    // 회원정보 확인
    @GetMapping("/me")
    public ResponseEntity<?> getMyInfo(@RequestHeader("Authorization") String authHeader) {
        try {
            Long userId = getUserId(authHeader);
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            return ResponseEntity.ok(new UserInfoDto(user));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // 비밀번호 변경
    @PutMapping("/password")
    public ResponseEntity<String> changePassword(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody ChangePasswordDto dto) {
        Long userId = getUserId(authHeader);
        User user = userRepository.findById(userId).orElseThrow();

        if (!user.getPassword().equals(dto.getCurrentPassword())) {
            return ResponseEntity.badRequest().body("현재 비밀번호가 일치하지 않습니다.");
        }
        user.setPassword(dto.getNewPassword());
        userRepository.save(user);
        return ResponseEntity.ok("비밀번호가 변경되었습니다.");
    }

    // 회원탈퇴
    @DeleteMapping("/me")
    public ResponseEntity<String> deleteMe(@RequestHeader("Authorization") String authHeader) {
        Long userId = getUserId(authHeader);
        userRepository.deleteById(userId);
        return ResponseEntity.ok("회원탈퇴가 완료되었습니다.");
    }

    // 주문내역 조회
    @GetMapping("/orders")
    public ResponseEntity<?> getMyOrders(@RequestHeader("Authorization") String authHeader) {
        try {
            Long userId = getUserId(authHeader);
            List<OrderDto> orders = orderService.getOrdersByUser(userId);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}