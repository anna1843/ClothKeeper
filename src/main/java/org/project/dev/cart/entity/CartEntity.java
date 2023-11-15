package org.project.dev.cart.entity;

import lombok.*;
import org.project.dev.member.entity.MemberEntity;
import org.project.dev.member.entity.SemiMemberEntity;
import org.project.dev.utils.BaseEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "cart_project")
public class CartEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long cartId;

    // 카트에 담긴 총 상품 갯수
    @Column(name = "cart_count")
    private int cartCount;

    // 구매자, 일반회원
    @OneToOne(fetch = FetchType.LAZY) // 뜻 알아보기
    @JoinColumn(name = "member_id")
    private MemberEntity member;

    // 간편회원
    @OneToOne(fetch = FetchType.LAZY) // 뜻 알아보기
    @JoinColumn(name = "semiMember_id")
    private SemiMemberEntity semiMember;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    private List<CartItemEntity> cartItems = new ArrayList<>();

    public static CartEntity createCart(MemberEntity member){
        CartEntity cart=new CartEntity();
        cart.setCartCount(0);
        cart.setMember(member);
        return cart;
    }

    // 간편회원
    public static CartEntity createSemiCart(SemiMemberEntity semiMember){
        CartEntity cart=new CartEntity();
        cart.setCartCount(0);
        cart.setSemiMember(semiMember);
        return cart;
    }



}
