package org.project.dev.member.controller;

import lombok.RequiredArgsConstructor;
import org.project.dev.member.dto.EmailPostDto;
import org.project.dev.member.dto.EmailResponseDto;
import org.project.dev.member.entity.EmailMessageEntity;
import org.project.dev.member.service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/send-mail")
public class EmailController {

    private final EmailService emailService;

    // 임시 비밀번호 발급
    @PostMapping("/password")
    public ResponseEntity postSendPasswordMail(@RequestBody EmailPostDto emailPostDto) {
        EmailMessageEntity emailMessageEntity = EmailMessageEntity.builder()
                .to(emailPostDto.getEmail())
                .subject("<<SWC_test>> 임시 비밀번호 발급")
                .build();

        emailService.sendMail(emailMessageEntity, "password");

//        return ResponseEntity.ok().build();
        return ResponseEntity.ok("{}"); // 빈 JSON 객체 반환, 클라이언트에서 JSON 파싱 오류를 피하기 위해 빈 JSON 객체로 응답을 반환하는 것이 일반적
    }

    // 회원가입 이메일 인증 - 요청 시 body로 인증번호 반환하도록 작성하였음
    @PostMapping("/email")
    public ResponseEntity postSendJoinMail(@RequestBody EmailPostDto emailPostDto) {
        EmailMessageEntity emailMessageEntity = EmailMessageEntity.builder()
                .to(emailPostDto.getEmail())
                .subject("<<SWC_test>> 이메일 인증을 위한 인증 코드 발송")
                .build();

        String code = emailService.sendMail(emailMessageEntity, "email");

        EmailResponseDto emailResponseDto = new EmailResponseDto();
        emailResponseDto.setCode(code);

        return ResponseEntity.ok(emailResponseDto);
    }
}
