package com.example.shop.controller;

import com.example.shop.dto.CartItemDto;
import com.example.shop.dto.QuantityDto;
import com.example.shop.entity.CartItem;
import com.example.shop.service.CartService;
import com.example.shop.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    private final JwtUtil jwtUtil;

    //  토큰에서 사용자 ID 추출
    private Long extractUserId(String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        return jwtUtil.extractUserId(token);
    }

    // 장바구니 조회
    @GetMapping
    public List<CartItemDto> getCart(@RequestHeader("Authorization") String authHeader) {
        return cartService.getCartItems(extractUserId(authHeader)).stream()
                .map(CartItemDto::fromEntity)
                .collect(Collectors.toList());
    }

    // 상품 추가 (POST /api/cart/{productId})
    @PostMapping("/{productId}")
    public void addToCart(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long productId,
            @RequestBody QuantityDto quantityDto) {
        cartService.addOrUpdateItem(
                extractUserId(authHeader),
                productId,
                quantityDto.getQuantity()
        );
    }

    // 수량 수정 (PUT /api/cart/{productId})
    @PutMapping("/{productId}")
    public void updateQuantity(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long productId,
            @RequestBody QuantityDto quantityDto) {
        cartService.addOrUpdateItem(
                extractUserId(authHeader),
                productId,
                quantityDto.getQuantity()
        );
    }

    // 개별 삭제
    @DeleteMapping("/{productId}")
    public void removeItem(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long productId) {
        cartService.removeItem(extractUserId(authHeader), productId);
    }

    // 전체 삭제
    @DeleteMapping
    public void clearCart(@RequestHeader("Authorization") String authHeader) {
        cartService.clearCart(extractUserId(authHeader));
    }
}
