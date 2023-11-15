package org.project.dev.member.repository;


import org.project.dev.constrant.Role;
import org.project.dev.member.entity.MemberEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    Optional<MemberEntity> findByMemberEmail(String memberEmail);

//    boolean existsByMemberEmail(String memberEmail);

    boolean existsByMemberNickName(String memberNickName);

    boolean existsByMemberPhone(String memberPhone);
    
    // 이메일 찾기
    MemberEntity findByMemberNickNameAndMemberPhone(String memberNickName, String memberPhone);
    
    // 비밀번호 찾기
    MemberEntity findByMemberEmailAndMemberPhone(String memberEmail, String memberPhone);

    // 회원목록 내 검색
    Page<MemberEntity> findByMemberEmailContaining(Pageable pageable, String memberEmail);
    Page<MemberEntity> findByMemberNameContaining(Pageable pageable, String memberEmail);
    Page<MemberEntity> findByMemberNickNameContaining(Pageable pageable, String memberNickName);
    Page<MemberEntity> findByMemberPhoneContaining(Pageable pageable, String memberPhone);
    Page<MemberEntity> findByMemberBirthContaining(Pageable pageable, String memberBirth);
    Page<MemberEntity> findByMemberStreetAddressContaining(Pageable pageable, String memberStreetAddress);
    Page<MemberEntity> findByMemberDetailAddressContaining(Pageable pageable, String memberDetailAddress);
    Page<MemberEntity> findByRoleContaining(Pageable pageable, Role Role);

    // 일반회원, 간편회원 한 테이블에 모두 출력, 값 없으면 null 처리
//    @Query("SELECT 'member' AS member, m.memberEmail AS memberEmail, " +
//            "m.memberNickName AS memberNickName, m.memberName AS memberName, " +
//            "m.memberBirth AS memberBirth, m.memberPhone AS memberPhone, " +
//            "m.memberDetailAddress AS memberDetailAddress, " +
//            "m.memberStreetAddress AS memberStreetAddress " +
//            "FROM MemberEntity m " +
//            "UNION ALL " +
//            "SELECT 'semiMember' AS member, s.semiMemberEmail AS memberEmail, " +
//            "NULL AS memberNickName, NULL AS memberName, NULL AS memberBirth, " +
//            "NULL AS memberPhone, NULL AS memberDetailAddress, NULL AS memberStreetAddress " +
//            "FROM SemiMemberEntity s")
//    List<CombineDto> getCombinedMemberAndSemiMemberInfo();
}


