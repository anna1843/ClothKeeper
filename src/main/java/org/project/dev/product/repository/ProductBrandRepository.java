package org.project.dev.product.repository;

import org.project.dev.product.entity.ProductBrandEntity;
import org.project.dev.product.entity.ProductEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductBrandRepository extends JpaRepository<ProductBrandEntity, Long>{

    /*
    Todo
     1. code.aiaru@gmail.com
     2. 상품 브랜드 관련 Repository 입니다.
        JPQL 쿼리문은 이 곳에 위치하게 됩니다.
     3. x
     4. x
     */

    // 브랜드 명으로 찾기.
    Optional<ProductBrandEntity> findByProductBrandName(String productBrandName);

    // 브랜드 명으로 찾기. (containing)
    List<ProductBrandEntity> findByProductBrandNameContaining(String substring);

}
