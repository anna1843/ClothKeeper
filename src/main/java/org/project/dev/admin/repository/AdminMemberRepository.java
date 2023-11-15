package org.project.dev.admin.repository;

import org.project.dev.admin.entity.AdminMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminMemberRepository extends JpaRepository<AdminMemberEntity,Long> {


}


