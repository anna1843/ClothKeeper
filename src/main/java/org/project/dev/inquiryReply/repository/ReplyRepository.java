package org.project.dev.inquiryReply.repository;

import org.project.dev.inquiryReply.entity.ReplyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReplyRepository extends JpaRepository<ReplyEntity,Long> {

    @Query("SELECT r FROM ReplyEntity r where r.inquiryEntity.id = :inqId")
    List<ReplyEntity> findByInquiryId(Long inqId);
}
