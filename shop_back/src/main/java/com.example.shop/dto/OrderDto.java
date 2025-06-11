package com.example.shop.dto;

import com.example.shop.entity.Order;
import com.example.shop.entity.OrderItem;
import lombok.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class OrderDto {
    private Long id;
    private String orderDate;
    private int totalPrice;
    private String status;
    private List<OrderItemDto> items;

    public static OrderDto fromEntity(Order order) {
        OrderDto dto = new OrderDto();
        dto.id = order.getId();
        dto.orderDate = order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        dto.totalPrice = order.getTotalPrice();
        dto.status = order.getStatus();
        dto.items = order.getOrderItems().stream()
                .map(OrderItemDto::fromEntity)
                .collect(Collectors.toList());
        return dto;
    }

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateOrderRequest {
        private List<OrderItemRequest> items;
        private String recipient;
        private String address;
        private String phone;
    }

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemRequest {
        private Long productId;
        private int quantity;
    }

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemDto {
        private String productName;
        private String productImageUrl;
        private int quantity;
        private int price;

        public static OrderItemDto fromEntity(OrderItem item) {
            OrderItemDto dto = new OrderItemDto();
            dto.productName = item.getProductName();
            dto.productImageUrl = item.getProductImageUrl();
            dto.quantity = item.getQuantity();
            dto.price = item.getPrice();
            return dto;
        }
    }
}
