package com.example.shop.service;

import com.example.shop.dto.OrderDto;
import com.example.shop.entity.Order;
import com.example.shop.entity.OrderItem;
import com.example.shop.entity.Product;
import com.example.shop.entity.User;
import com.example.shop.repository.CartItemRepository;
import com.example.shop.repository.OrderRepository;
import com.example.shop.repository.ProductRepository;
import com.example.shop.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;

    public List<OrderDto> getOrdersByUser(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream()
                .map(OrderDto::fromEntity)
                .collect(Collectors.toList());
    }
    @Transactional
    public Long createOrder(Long userId, OrderDto.CreateOrderRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = Order.builder()
                .user(user)
                .orderDate(LocalDateTime.now())
                .status("결제 완료")
                .recipient(request.getRecipient())
                .address(request.getAddress())
                .phone(request.getPhone())
                .totalPrice(0)
                .build();

        int totalPrice = 0;
        for (OrderDto.OrderItemRequest item : request.getItems()) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)  // 커스텀 빌더로 productName/imageUrl 자동 설정
                    .quantity(item.getQuantity())
                    .price(product.getPrice())
                    .build();

            order.getOrderItems().add(orderItem);
            totalPrice += product.getPrice() * item.getQuantity();
        }

        order.setTotalPrice(totalPrice);
        orderRepository.save(order);
        cartItemRepository.deleteByUserId(userId);
        return order.getId();
    }

    @Transactional
    public void detachUserFromOrders(User user) {
        List<Order> orders = orderRepository.findByUser(user);
        orders.forEach(order -> order.setUser(null));
        orderRepository.saveAll(orders);
    }

}
