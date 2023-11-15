package org.project.dev.notice.entity;

import lombok.*;
import org.project.dev.utils.BaseEntity;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "inquiry_img")
public class InquiryImgEntity extends BaseEntity {

    @Id // 기본키 설정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto increment
    @Column(name = "inquiry_img_id")
    private Long id;

    @Column(name = "inquiry_img_original_name")
    private String inquiryImgOriginalName; // 문의사항 이미지 등록 시 원본 이미지

    @Column(name = "inqquiry_img_save_name")
    private String inquiryImgSaveName; // 문의사항 이미지 등록 시 DB저장용 이미지

    // N:1 InquiryEntity
    // ing_id , inquiry_img_id
}
