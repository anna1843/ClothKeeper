package org.project.dev.inquiryReply.dto;


import lombok.*;
import org.project.dev.utils.BaseEntity;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ReplyDto extends BaseEntity {

    private Long id;

    private String reply;

    // 닉네임
    private String replyWriter;

    // 문의사항 아이디
    private Long inqId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;



}
