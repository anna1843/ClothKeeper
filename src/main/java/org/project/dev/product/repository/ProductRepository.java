package org.project.dev.product.repository;

import org.project.dev.member.entity.MemberEntity;
import org.project.dev.product.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long>, JpaSpecificationExecutor<ProductEntity> {

    /*
    Todo
     1. code.aiaru@gmail.com
     2. 상품 관련 Repository 입니다.
        JPQL 쿼리문은 이 곳에 위치하게 됩니다.
     3. x
     4. x
     */

    // HITS (조회수)
    @Modifying
    @Query(value = "update ProductEntity p set p.productHits=p.productHits+1 where p.id=:id")
    void updateHits(@Param("id") Long id);

    // DELETE (삭제)
    // 이름은 delete이지만, 실제로는 productDisplay 값을 false로 바꿔 사용자에게 보이지 않도록 처리.
    @Modifying
    @Query("update ProductEntity p set p.isProductDisplayed = false where p.id = :id")
    void softDelete(@Param("id") Long id);

    // 가장 큰 id 값을 찾음.
    Optional<ProductEntity> findTopByOrderByIdDesc();



    // 송원철 / 장바구니 관련
    ProductEntity findProductById(Long id);

    // 송원철 / 회원 삭제 시 조회 
    List<ProductEntity> findByMember(MemberEntity member);
}
