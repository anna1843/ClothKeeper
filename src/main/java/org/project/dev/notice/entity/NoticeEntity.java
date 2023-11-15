package org.project.dev.notice.entity;


import lombok.*;
import org.project.dev.notice.dto.NoticeDto;
import org.project.dev.utils.BaseEntity;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "notice")
public class NoticeEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 1개씩 증가하면서 자동생성
    @Column(name = "not_id")
    private Long notId; // 공지사항 ID
    // 생성될 떄마다 아이디가 부여됨
    @Column(name = "not_type")
    private String notType; // 공지사항 종류
    @Column(name = "not_title", nullable = false)
    private String noticeTitle; // 공지사항 글 제목
    @Column(name = "not_Content", nullable = false)
    private String noticeContent; // 공지사항 글 내용
    @Column(name = "not_Writer", nullable = false)
    private String notWriter; // 공지사항 글 작성자
    @Column(name = "not_Hit", nullable = false)
    private int notHit; // 공지사항 글 조회수


    public static NoticeEntity toNoticeEntityInsert(NoticeDto noticeDto) {
        NoticeEntity noticeEntity = new NoticeEntity();
        noticeEntity.setNotType(noticeDto.getNotType());
        noticeEntity.setNoticeTitle(noticeDto.getNoticeTitle());
        noticeEntity.setNoticeContent(noticeDto.getNoticeContent());
        noticeEntity.setNotWriter(noticeDto.getNotWriter());
        noticeEntity.setNotHit(0);
        return noticeEntity;
    }

    // Dto -> Entitty
    public static NoticeEntity toNoticeEntityUpdate(NoticeDto noticeDto) {
        NoticeEntity noticeEntity = new NoticeEntity();
        noticeEntity.setNotId(noticeDto.getNotId());
        noticeEntity.setNotType(noticeDto.getNotType());
        noticeEntity.setNoticeTitle(noticeDto.getNoticeTitle());
        noticeEntity.setNoticeContent(noticeDto.getNoticeContent());
        noticeEntity.setNotWriter(noticeDto.getNotWriter());
        noticeEntity.setNotHit(noticeDto.getNotHit());
        noticeEntity.setCreateTime(noticeDto.getCreateTime());
        noticeEntity.setUpdateTime(noticeDto.getUpdateTime());
        return noticeEntity;
    }

    // 연관관계
    // 1:N 관리자
}
