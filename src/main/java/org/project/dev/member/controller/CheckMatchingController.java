package org.project.dev.member.controller;

import lombok.RequiredArgsConstructor;
import org.project.dev.member.dto.MemberDto;
import org.project.dev.member.service.MemberService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CheckMatchingController {

    private final MemberService memberService;

    // 비밀번호 찾기 기능에서 이메일과 휴대전화번호 둘다 일치하는지
    @GetMapping("/check-emailPhoneMatching")
    public MemberDto checkEmailPhoneMatching(@RequestParam String memberEmail, @RequestParam String memberPhone) {

        boolean isMatching = memberService.checkEmailPhoneMatching(memberEmail, memberPhone);
        return new MemberDto(isMatching);
    }

}
