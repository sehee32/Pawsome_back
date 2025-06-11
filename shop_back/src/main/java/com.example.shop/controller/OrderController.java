package com.example.shop.controller;

import com.example.shop.dto.OrderDto;
import com.example.shop.service.OrderService;
import com.example.shop.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<Long> createOrder(
            @RequestHeader("Authorization") String token,
            @RequestBody OrderDto.CreateOrderRequest request
    ) {
        Long userId = jwtUtil.extractUserId(token.replace("Bearer ", ""));
        Long orderId = orderService.createOrder(userId, request);
        return ResponseEntity.ok(orderId);
    }
}
