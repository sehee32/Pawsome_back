package com.example.shop.repository;

import com.example.shop.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;


public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    // 사용자 ID와 상품 ID로 장바구니 항목 찾기 (JPQL 사용)
    @Query("SELECT ci FROM CartItem ci WHERE ci.user.id = :userId AND ci.product.id = :productId")
    Optional<CartItem> findByUserIdAndProductId(@Param("userId") Long userId, @Param("productId") Long productId);

    // 사용자 ID로 모든 장바구니 항목 조회
    List<CartItem> findByUserId(Long userId);

    // 사용자 ID와 상품 ID로 삭제
    void deleteByUserIdAndProductId(Long userId, Long productId);

    // 사용자 ID로 전체 삭제
    void deleteByUserId(Long userId);
}
