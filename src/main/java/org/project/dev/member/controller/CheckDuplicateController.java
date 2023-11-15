package org.project.dev.member.controller;

import lombok.RequiredArgsConstructor;
import org.project.dev.member.exception.BadRequestException;
import org.project.dev.member.service.MemberService;
import org.project.dev.member.service.SemiMemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class CheckDuplicateController {

    private final MemberService memberService;
    private final EntityManager entityManager;

//    @GetMapping("/memberEmail/check")
//    public ResponseEntity<?> getCheckEmailDuplication(@RequestParam(value = "memberEmail") String memberEmail) throws BadRequestException {
//
//        System.out.println(memberEmail);
//
//        if(memberService.existsByMemberEmail(memberEmail) == true){
//            throw new BadRequestException("이미 사용중인 이메일입니다");
//        }else{
//            return ResponseEntity.ok("사용가능한 이메일입니다");
//        }
//    }

    // 직접 두 테이블 조회해 이메일 중복 검사
    @GetMapping("/memberEmail/check")
    public ResponseEntity<?> getCheckEmailDuplication(@RequestParam(value = "memberEmail") String memberEmail) throws BadRequestException {
        String jpql = "SELECT COUNT(m) " +
                "FROM MemberEntity m " +
                "WHERE m.memberEmail = :email";

        String semiJpql = "SELECT COUNT(s) " +
                "FROM SemiMemberEntity s " +
                "WHERE s.semiMemberEmail = :email";

        TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class)
                .setParameter("email", memberEmail);

        TypedQuery<Long> semiQuery = entityManager.createQuery(semiJpql, Long.class)
                .setParameter("email", memberEmail);

        Long count = query.getSingleResult() + semiQuery.getSingleResult();

        if (count > 0) {
            throw new BadRequestException("이미 사용중인 이메일입니다");
        } else {
            return ResponseEntity.ok("사용가능한 이메일입니다");
        }
    }

    @GetMapping("/memberNickName/check")
    public ResponseEntity<?> getCheckNickNameDuplication(@RequestParam(value = "memberNickName") String memberNickName) throws BadRequestException {
        System.out.println(memberNickName);

        if (memberService.existsByMemberNickName(memberNickName) == true) {
            throw new BadRequestException("이미 사용중인 닉네임입니다");
        }else{
            return ResponseEntity.ok("사용가능한 닉네임입니다");
        }
    }

    @GetMapping("/memberPhone/check")
    public ResponseEntity<?> getCheckPhoneDuplication(@RequestParam(value = "memberPhone") String memberPhone) throws BadRequestException {
        String jpql = "SELECT COUNT(m) " +
                "FROM MemberEntity m " +
                "WHERE m.memberPhone = :phone";

        String semiJpql = "SELECT COUNT(s) " +
                "FROM SemiMemberEntity s " +
                "WHERE s.semiMemberPhone = :phone";

        TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class)
                .setParameter("phone", memberPhone);

        TypedQuery<Long> semiQuery = entityManager.createQuery(semiJpql, Long.class)
                .setParameter("phone", memberPhone);

        Long count = query.getSingleResult() + semiQuery.getSingleResult();

        if (count > 0) {
            throw new BadRequestException("이미 사용중인 휴대전화번호입니다");
        } else {
            return ResponseEntity.ok("사용가능한 휴대전화번호입니다");
        }
    }
}
