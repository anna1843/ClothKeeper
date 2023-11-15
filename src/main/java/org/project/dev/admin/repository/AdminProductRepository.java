package org.project.dev.admin.repository;

import org.project.dev.admin.entity.AdminProductEntity;
import org.project.dev.member.entity.MemberEntity;
import org.project.dev.product.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/*


 */
@Repository
public interface AdminProductRepository extends JpaRepository<ProductEntity,Long>  {

    @Query(value = "select * from product_table1 where member_id = :memberId", nativeQuery = true)
    List<ProductEntity> getMemberIdProductList(int memberId);

    @Modifying
    @Query(value = "update ProductEntity p set p.productHits=p.productHits+1 where p.id=:id")
    void updateHits(@Param("id") Long id);

    // DELETE (삭제)
    // 이름은 delete이지만, 실제 로직은 update.
    // productDisplay 값을 false로 바꿔, 사용자에게 보이지 않도록 처리합니다.
    @Modifying
    @Query("update ProductEntity p set p.isProductDisplayed = false where p.id = :id")
    void softDelete(@Param("id") Long id);

    // Cursor-Based List
    @Query(value = "SELECT p FROM ProductEntity p ORDER BY p.id DESC")
    List<ProductEntity> findProductsByOrderByIdDesc(Pageable pageable);

    @Query(value = "SELECT p FROM ProductEntity p WHERE p.id < :lastId ORDER BY p.id DESC")
    List<ProductEntity> findProductsByIdLessThanOrderByIdDesc(@Param("lastId") Long lastId, Pageable pageable);

    // 송원철 / 장바구니 관련
    ProductEntity findProductById(Long id);

    // 송원철 / 회원 삭제 시 조회
    List<ProductEntity> findByMember(MemberEntity member);

    Page<ProductEntity> findAll(Specification<ProductEntity> entitySpecification, Pageable pageable);
}
