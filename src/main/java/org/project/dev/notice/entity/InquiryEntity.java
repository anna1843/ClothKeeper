package org.project.dev.notice.entity;

import lombok.*;
import org.project.dev.inquiryReply.entity.ReplyEntity;
import org.project.dev.member.entity.MemberEntity;
import org.project.dev.member.entity.SemiMemberEntity;
import org.project.dev.notice.dto.InquiryDto;
import org.project.dev.review.entity.ReviewEntity;
import org.project.dev.utils.BaseEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "inquiry") // 송원철
public class InquiryEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inq_id")
    private Long inqId; // 공지사항 ID
    // inqID
    @Column(name = "inq_type")
    private String inqType;

    @Column(name = "inq_title", nullable = false)
    private String inquiryTitle; // 문의사항 글 제목

    @Column(name = "inq_Content", nullable = false)
    private String inquiryContent; // 문의사항 글 내용

//    @Column(name = "inq_Writer", nullable = false)
//    private String inquiryWriter; // 문의사항 글 작성자

    @Column(name = "inq_Hit", nullable = false)
    private int inqHit; // 문의사항 글 조회수

    @Column(nullable = false, length = 1)
    private int inquiryFile; // file이 존재하면 1, 없으면 0

    // 연관관계 / 송원철
    @ManyToOne
    @JoinColumn(name = "member_id")
    private MemberEntity member;

    // DB 연관관계 설정 -> ReviewEntity
    @OneToMany(mappedBy = "inquiryEntity",cascade = CascadeType.REMOVE)
    private List<ReplyEntity> replyEntityList = new ArrayList<>();

    public static InquiryEntity toInquiryEntityInsert(InquiryDto inquiryDto) {
        InquiryEntity inquiryEntity = new InquiryEntity();
        inquiryEntity.setInqType(inquiryDto.getInqType());
        inquiryEntity.setInquiryTitle(inquiryDto.getInquiryTitle());
        inquiryEntity.setInquiryContent(inquiryDto.getInquiryContent());
        inquiryEntity.setInqHit(0);

        // 송원철 / 문의사항 등록 시 memberId 가져오기
        if (inquiryDto.getMember() != null) {
            MemberEntity memberEntity = new MemberEntity();
            memberEntity.setMemberId(inquiryDto.getMember().getMemberId());
            inquiryEntity.setMember(memberEntity);
        }

        return inquiryEntity;
    }

    public static InquiryEntity toInquiryEntityUpdate(InquiryDto inquiryDto) {
        InquiryEntity inquiryEntity = new InquiryEntity();
        inquiryEntity.setInqId(inquiryDto.getInqId());
        inquiryEntity.setInqType(inquiryDto.getInqType());
        inquiryEntity.setInquiryTitle(inquiryDto.getInquiryTitle());
        inquiryEntity.setInquiryContent(inquiryDto.getInquiryContent());
//        inquiryEntity.setInquiryWriter(inquiryDto.getInquiryWriter());
        inquiryEntity.setInqHit(inquiryDto.getInqHit());
        inquiryEntity.setUpdateTime(inquiryDto.getUpdateTime());
        return inquiryEntity;
    }

}
