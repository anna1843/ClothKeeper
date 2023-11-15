package org.project.dev.inquiryReply.entity;


import lombok.*;
import org.project.dev.notice.entity.InquiryEntity;
import org.project.dev.utils.BaseEntity;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Getter
@Setter
@Table(name = "inquiry_reply")
public class ReplyEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_id")
    private Long id;

    @Column(nullable = false, name = "reply_content")
    private String reply;

    @Column(name = "reply_writer", nullable = false)
    private String replyWriter;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "inq_id")
    private InquiryEntity inquiryEntity;



}
