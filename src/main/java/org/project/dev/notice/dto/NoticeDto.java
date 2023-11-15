package org.project.dev.notice.dto;

import lombok.*;
import org.project.dev.notice.entity.NoticeEntity;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class NoticeDto {
    private Long notId; // 공지사항 아이디
    private String notType; // 공지사항 분류
    private String noticeTitle; // 공지사항 글 제목
    private String noticeContent; // 공지사항 글 내용
    private String notWriter; // 공지사항 글 작성자
    private int notHit;  // 공지사항 글 조회수
    private LocalDateTime CreateTime; // 공지사항 생성시간
    private LocalDateTime UpdateTime; // 공지사항 수정시간

    public static NoticeDto tonoticeDto(NoticeEntity noticeEntity) {
        NoticeDto noticeDto = new NoticeDto();
        noticeDto.setNotId(noticeEntity.getNotId());
        noticeDto.setNotType(noticeEntity.getNotType());
        noticeDto.setNoticeTitle(noticeEntity.getNoticeTitle());
        noticeDto.setNoticeContent(noticeEntity.getNoticeContent());
        noticeDto.setNotWriter(noticeEntity.getNotWriter());
        noticeDto.setNotHit(noticeEntity.getNotHit());
        noticeDto.setCreateTime(noticeEntity.getCreateTime());
        noticeDto.setUpdateTime(noticeEntity.getUpdateTime());
        return noticeDto;
    }
}
