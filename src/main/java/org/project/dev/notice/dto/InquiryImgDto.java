package org.project.dev.notice.dto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class InquiryImgDto {

    private Long id;
    private String inquiryImgOriginalName; // 문의사항 이미지 등록 시 원본 이미지
    private String inquiryImgSaveName;
    private LocalDateTime CreateTime;
    private LocalDateTime UpdateTime;

}
