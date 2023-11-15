package org.project.dev.admin.dto;

import lombok.*;
import org.project.dev.admin.entity.AdminMemberEntity;
import org.project.dev.constrant.Role;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AdminMemberDto {

    private Long memberId;

    @NotBlank
    @Size(min = 10, max = 255)
    private String memberEmail;

    @NotBlank
    @Size(min = 8, max = 255)
    private String memberPassword;

    @NotBlank
    @Size(min = 2, max = 50)
    private String memberName;

    @NotBlank
    @Size(min = 2, max = 15)
    private String memberNickName;

    @NotBlank
    @Size(min = 10, max = 11)
    private String memberPhone;

    @NotBlank
    @Size(min = 8, max = 8)
    private String memberBirth;

    @NotBlank
    @Size(min = 2, max = 255)
    private String memberAddress;

    private Role role;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    public static AdminMemberDto toDto(AdminMemberEntity memberEntity) {

        return AdminMemberDto.builder()
                .memberId(memberEntity.getMemberId())
                .memberEmail(memberEntity.getMemberEmail())
                .memberPassword(memberEntity.getMemberPassword()).
                memberName(memberEntity.getMemberName()).
                memberNickName(memberEntity.getMemberNickName()).
                memberPhone(memberEntity.getMemberPhone()).
                memberBirth(memberEntity.getMemberBirth()).
                memberAddress(memberEntity.getMemberAddress()).
                role(memberEntity.getRole()).
                createTime(memberEntity.getCreateTime()).
                updateTime(memberEntity.getUpdateTime())
                .build();

    }

}
