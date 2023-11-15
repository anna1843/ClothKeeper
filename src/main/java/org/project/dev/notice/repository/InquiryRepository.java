package org.project.dev.notice.repository;


import org.project.dev.notice.entity.InquiryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InquiryRepository extends JpaRepository<InquiryEntity, Long> {
    Page<InquiryEntity> findByInquiryTitleContaining(Pageable pageable, String title); // 제목
    Page<InquiryEntity> findByInquiryContentContaining(Pageable pageable, String content); // 내용
    Page<InquiryEntity> findByMemberMemberEmailContaining(Pageable pageable, String memberEmail); // 송원철 / 이메일
    Page<InquiryEntity> findByMemberMemberId(Pageable pageable, Long memberId); // 송원철 // 내가 쓴 문의사항 가져오기
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE inquiry i set i.inq_hit = i.inq_hit+1 where i.inq_id = :id", nativeQuery = true)
    void InquiryHit(@Param("id") Long id);


}

