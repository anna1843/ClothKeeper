package org.project.dev.notice.repository;


import org.project.dev.notice.entity.NoticeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepository extends JpaRepository<NoticeEntity, Long> {

    Page<NoticeEntity> findByNoticeTitleContaining(Pageable pageable, String title); // paging & list & search -> title
    Page<NoticeEntity> findByNoticeContentContaining(Pageable pageable, String content); // // paging & list & search -> content
    Page<NoticeEntity> findByNotType(String type, Pageable pageable); // paging & list -> type

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE notice n set n.not_hit = n.not_hit + 1 where n.not_id = :id" , nativeQuery = true)
    void NoticeHit(@Param("id") Long id);

}
