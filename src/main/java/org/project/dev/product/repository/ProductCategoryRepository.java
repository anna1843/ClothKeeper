package org.project.dev.product.repository;

import org.project.dev.product.entity.ProductCategoryEntity;
import org.project.dev.product.entity.ProductCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategoryEntity, Long>{

    /*
    Todo
     1. code.aiaru@gmail.com
     2. 상품 카테고리 관련 Repository 입니다.
        JPQL 쿼리문은 이 곳에 위치하게 됩니다.
     3. x
     4. x
     */

    // 카테고리 명으로 찾기.
    Optional<ProductCategoryEntity> findByProductCategoryName(String productCategoryName);

    // 카테고리 명으로 찾기. (containing)
    List<ProductCategoryEntity> findByProductCategoryNameContaining(String substring);


}
