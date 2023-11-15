package org.project.dev.admin.service;

import lombok.RequiredArgsConstructor;
import org.project.dev.admin.dto.AdminMemberDto;
import org.project.dev.admin.entity.AdminMemberEntity;
import org.project.dev.admin.repository.AdminMemberRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminMemberService {

    private final AdminMemberRepository adminMemberRepository;

    public List<AdminMemberDto> getMemberList() {
        List<AdminMemberDto> adminMemberDtoList = new ArrayList<>();
        List<AdminMemberEntity> memberEntityList = adminMemberRepository.findAll();

        for (AdminMemberEntity memberEntity : memberEntityList) {
            adminMemberDtoList.add(AdminMemberDto.toDto(memberEntity));
        }
        return adminMemberDtoList;
    }

    public AdminMemberDto getMemberDetail(Long memberId) {

        Optional<AdminMemberEntity> adminMemberEntity = Optional.ofNullable(adminMemberRepository.findById(memberId).orElseThrow(() -> {
            throw new IllegalArgumentException("찾는 해당 멤버 아이디가 없습니다");
        }));
        return AdminMemberDto.toDto(adminMemberEntity.get());
    }


    public String adminMemberInsert(AdminMemberDto adminMemberDto) {
        Long result = adminMemberRepository.save(AdminMemberEntity.toEntity(adminMemberDto)).getMemberId();
        if (result == null) {
            throw new RuntimeException("Member 생성중 id 가 null로 반환됨!");
        }
        return "ok";
    }

    public String adminMemberUpdate(AdminMemberDto adminMemberDto) {
        Long result = adminMemberRepository.save(AdminMemberEntity.toEntity(adminMemberDto)).getMemberId();
        if (result == null) {
            throw new RuntimeException("Member 생성중 id 가 null로 반환됨!");
        }
        return "ok";
    }

}
