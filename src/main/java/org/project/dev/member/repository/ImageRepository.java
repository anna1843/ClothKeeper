package org.project.dev.member.repository;

import org.project.dev.member.dto.ImageResponseDto;
import org.project.dev.member.dto.ImageUploadDto;
import org.project.dev.member.entity.ImageEntity;
import org.project.dev.member.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<ImageEntity, Long> {

    ImageEntity findByMember(MemberEntity member);

}
