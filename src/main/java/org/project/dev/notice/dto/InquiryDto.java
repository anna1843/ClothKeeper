package org.project.dev.notice.dto;

import lombok.*;
import org.project.dev.member.entity.MemberEntity;
import org.project.dev.notice.entity.InquiryEntity;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class InquiryDto {
    private Long inqId; // 공지사항 아이디
    private String inqType;
    private String inquiryTitle; // 공지사항 글 제목
    private String inquiryContent; // 공지사항 글 내용
    private int inqHit;  // 공지사항 글 조회수
    private LocalDateTime CreateTime; // 공지사항 생성시간
    private LocalDateTime UpdateTime; // 공지사항 수정시간

    // 송원철 / 연관관계
    private MemberEntity member;
    // 송원철 / memberEmail 출력 용도
    private String memberEmail;

    public static InquiryDto toinquiryDto(InquiryEntity inquiryEntity) {
        InquiryDto inquiryDto = new InquiryDto();
        inquiryDto.setInqId(inquiryEntity.getInqId());
        inquiryDto.setInqType(inquiryEntity.getInqType());
        inquiryDto.setInquiryTitle(inquiryEntity.getInquiryTitle());
        inquiryDto.setInquiryContent(inquiryEntity.getInquiryContent());
        inquiryDto.setInqHit(inquiryEntity.getInqHit());
        inquiryDto.setCreateTime(inquiryEntity.getCreateTime());
        inquiryDto.setUpdateTime(inquiryEntity.getUpdateTime());
        // member 필드 초기화
        if (inquiryEntity.getMember() != null) {
            inquiryDto.setMemberEmail(inquiryEntity.getMember().getMemberEmail());
        }
        inquiryDto.setMemberEmail(inquiryEntity.getMember().getMemberEmail()); // 송원철

        return inquiryDto;

    }
}

