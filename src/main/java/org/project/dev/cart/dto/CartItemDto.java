package org.project.dev.cart.dto;

import lombok.*;
import org.project.dev.cart.entity.CartEntity;
import org.project.dev.product.entity.ProductEntity;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemDto {

    private Long cartItemId;

    private CartEntity cart;

    // 상품
    private ProductEntity product;

    // 상품 갯수
    private int cartItemCount;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
