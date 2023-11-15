package org.project.dev.admin.entity;


import lombok.*;
import org.project.dev.admin.dto.AdminMemberDto;
import org.project.dev.constrant.Role;
import org.project.dev.utils.BaseEntity;

import javax.persistence.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "admin_member_project")
public class AdminMemberEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId; // member_id로 쓸지 memberId로 쓸지 정하기

    @Column(name = "member_email", unique = true, nullable = false)
    private String memberEmail;

    @Column(name = "member_password", nullable = false)
    private String memberPassword;

    // 본인 이름
    @Column(name = "member_name", nullable = false)
    private String memberName;

    @Column(name = "member_nickName", unique = true, nullable = false)
    private String memberNickName;

    // 휴대전화번호
    @Column(name = "member_phone", nullable = false)
    private String memberPhone;

    // 생년월일
    @Column(name = "member_birth", nullable = false)
    private String memberBirth;

    // 주소
    @Column(name = "member_address", nullable = false)
    private String memberAddress;

    @Enumerated(EnumType.STRING)
    private Role role;


    public static AdminMemberEntity toEntity(AdminMemberDto adminMemberDto){
        return AdminMemberEntity
                .builder()
                .role(adminMemberDto.getRole())
                .memberPhone(adminMemberDto.getMemberPhone())
                .memberAddress(adminMemberDto.getMemberAddress())
                .memberId(adminMemberDto.getMemberId())
                .memberBirth(adminMemberDto.getMemberBirth())
                .memberEmail(adminMemberDto.getMemberEmail())
                .memberName(adminMemberDto.getMemberName())
                .memberNickName(adminMemberDto.getMemberNickName())
                .memberPassword(adminMemberDto.getMemberPassword())
                .build();
    }
}
