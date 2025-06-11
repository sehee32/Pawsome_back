package com.example.shop.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false)
    private String productName;

    @Column
    private String productImageUrl;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private int price;


    public static class OrderItemBuilder {
        private Product product;
        private String productName;
        private String productImageUrl;

        public OrderItemBuilder product(Product product) {
            this.product = product;
            if (product != null) {
                this.productName = product.getName();
                this.productImageUrl = product.getImageUrl();
            }
            return this;
        }
    }
}
