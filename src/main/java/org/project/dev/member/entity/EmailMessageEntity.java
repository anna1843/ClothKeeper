package org.project.dev.member.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

// 회원가입 시 이메일 인증 관련 entity

@Getter
@Setter
@Builder
public class EmailMessageEntity {

    private String to; // 수신자
    private String subject; // 제목
    private String message; // 내용
}