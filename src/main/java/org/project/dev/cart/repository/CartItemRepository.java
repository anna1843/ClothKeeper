package org.project.dev.cart.repository;

import org.project.dev.cart.entity.CartItemEntity;
import org.project.dev.product.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItemEntity, Long> {

    CartItemEntity findByCart_CartIdAndProduct_Id(Long cartId, Long productId);

    CartItemEntity findCartItemByCartItemId(Long cartItemId);

    List<CartItemEntity> findListCartItemByCartItemId(Long cartItemId);

    void deleteByCartItemId(Long cartItemId);

    // 회원 삭제 시 필요
    void deleteByProduct(ProductEntity product);
}
