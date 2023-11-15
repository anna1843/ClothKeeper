package org.project.dev.cart.entity;

import lombok.*;
import org.project.dev.product.entity.ProductEntity;
import org.project.dev.utils.BaseEntity;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "cartItem_project")
public class CartItemEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cartItem_id")
    private Long cartItemId;

    @ManyToOne(fetch = FetchType.LAZY) // 뜻 알아보기
    @JoinColumn(name = "cart_id")
    private CartEntity cart;

    // 상품
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    // 상품 갯수
    @Column(name = "cartItem_count")
    private int cartItemCount;

    public static CartItemEntity createCartItem(CartEntity cart, ProductEntity product, int amount){

        CartItemEntity cartItem=new CartItemEntity();
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setCartItemCount(amount);

        return cartItem;
    }

    // 이미 담겨있는 물건 또 담을 경우 수량 증가
    public void addCount(int cartItemCount){
        this.cartItemCount+=cartItemCount;
    }

}
