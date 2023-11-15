package org.project.dev.member.controller;

import lombok.RequiredArgsConstructor;
import org.project.dev.member.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

// 닉네임, 휴대전화번호 이용해 이메일 찾기
@Controller
@RequiredArgsConstructor
public class FindEmailController {

    private final MemberService memberService;

    // 닉네임과 휴대전화번호를 입력하는 폼 화면
    @GetMapping("/forgot-email")
    public String getShowForgotEmailForm() {
        return "member/findEmail";
    }

    @PostMapping("/find-email")
    @ResponseBody
    public ResponseEntity<String> postFindEmailByNicknameAndPhone(
            @RequestParam String memberNickName,
            @RequestParam String memberPhone) {

        String foundEmail = memberService.findEmailByNicknameAndPhone(memberNickName, memberPhone);

        if (foundEmail != null) {
            return ResponseEntity.ok(foundEmail);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("EmailNotFound");
        }
    }

}
