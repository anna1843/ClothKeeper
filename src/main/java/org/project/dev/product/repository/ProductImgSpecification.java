package org.project.dev.product.repository;

import org.project.dev.product.entity.ProductEntity;
import org.project.dev.product.entity.ProductImgEntity;
import org.springframework.data.jpa.domain.Specification;

public class ProductImgSpecification {

    /*
    Todo
     1. code.aiaru@gmail.com
     2. 상품 이미지 관련 Specification 클래스 입니다. Specification 쿼리들이 모여있는 곳 입니다.
        1) 삭제된 글(isProductImgDisplay=false)은 보이지 않도록 하는 쿼리.
     3. x
     4. x
    */

    // DB의 productDisplay 값이 true 일 때만 반환하는 쿼리
    public static Specification<ProductImgEntity> isDisplayTrue(){
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isProductImgDisplayed"),true);
    }

    // productId로 필터링하는 쿼리
    public static Specification<ProductImgEntity> byProductId(Long productId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("productEntity").get("id"), productId);
    }

}
