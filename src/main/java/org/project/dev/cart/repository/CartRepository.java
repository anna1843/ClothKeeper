package org.project.dev.cart.repository;

import org.project.dev.cart.entity.CartEntity;
import org.project.dev.member.entity.MemberEntity;
import org.project.dev.member.entity.SemiMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<CartEntity, Long> {

    CartEntity findByMember(MemberEntity member);

    CartEntity findCartByMember(MemberEntity member);

    // 간편회원
    CartEntity findBySemiMember(SemiMemberEntity semiMember);

    CartEntity findCartBySemiMember(SemiMemberEntity semiMember);

}
