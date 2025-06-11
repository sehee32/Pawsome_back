package com.example.shop.dto;
import com.example.shop.entity.CartItem;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class CartItemDto {
    private Long id;
    private int quantity;
    private ProductDto product;

    public static CartItemDto fromEntity(CartItem cartItem) {
        CartItemDto dto = new CartItemDto();
        dto.setId(cartItem.getId());
        dto.setQuantity(cartItem.getQuantity());
        dto.setProduct(ProductDto.fromEntity(cartItem.getProduct()));
        return dto;
    }
}
