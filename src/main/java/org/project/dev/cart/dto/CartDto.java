package org.project.dev.cart.dto;

import lombok.*;
import org.project.dev.cart.entity.CartItemEntity;
import org.project.dev.member.entity.MemberEntity;
import org.project.dev.member.entity.SemiMemberEntity;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartDto {

    private Long cartId;

    private MemberEntity member;

    // 간편회원
    private SemiMemberEntity semiMember;

    // 카트에 담긴 총 상품 갯수
    private int cartCount;

    private List<CartItemEntity> cartItems;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}
