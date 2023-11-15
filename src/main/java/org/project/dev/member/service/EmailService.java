package org.project.dev.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.dev.member.entity.EmailMessageEntity;
import org.project.dev.member.entity.MemberEntity;
import org.project.dev.member.repository.MemberRepository;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Optional;
import java.util.Random;

// 회원가입 시 이메일 인증 관련 Service
@Slf4j // log 객체 사용하기위함
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;
    private final MemberService memberService;
    private final MemberRepository memberRepository;

    public String sendMail(EmailMessageEntity emailMessageEntity, String type) {

        String authNum = createCode(); // 인증번호, 인증번호가 생성되면 해당 번호를 'authNum'에 저장하여 이메일 내용에 삽입하거나 임시비밀번호로 설정함

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        // 임시비밀번호 전송이 아닌 경우는 여기서 검사할 필요가 없습니다.
        if (type.equals("password")) {
            Optional<MemberEntity> optionalMemberEntity = memberRepository.findByMemberEmail(emailMessageEntity.getTo());
            if (!optionalMemberEntity.isPresent()) {
                log.error("해당 이메일을 가진 회원을 찾을 수 없습니다: {}", emailMessageEntity.getTo());
                throw new RuntimeException("해당 이메일을 가진 회원을 찾을 수 없습니다.");
            }
            memberService.SetTempPassword(emailMessageEntity.getTo(), authNum); // 임시비밀번호 저장
        }

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(emailMessageEntity.getTo()); // 메일 수신자
            mimeMessageHelper.setSubject(emailMessageEntity.getSubject()); // 메일 제목
            mimeMessageHelper.setText(setContext(authNum, type), true); // 메일 본문 내용, HTML 여부
            javaMailSender.send(mimeMessage);

            log.info("success");

            return authNum;

        } catch (MessagingException e) {
            log.info("fail");
            throw new RuntimeException(e);
        }
    }

    // 인증번호 및 임시 비밀번호 생성 메서드
    public String createCode() {
        Random random = new Random();
        StringBuffer key = new StringBuffer();

        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(4);

            switch (index) {
                case 0: key.append((char) ((int) random.nextInt(26) + 97)); break;
                case 1: key.append((char) ((int) random.nextInt(26) + 65)); break;
                default: key.append(random.nextInt(9));
            }
        }
        return key.toString();
    }

    // thymeleaf를 통한 html 적용
    public String setContext(String code, String type) {
        Context context = new Context();
        context.setVariable("code", code);
        return templateEngine.process(type, context);
    }

}
