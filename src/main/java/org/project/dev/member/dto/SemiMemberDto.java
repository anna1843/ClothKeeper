package org.project.dev.member.dto;

import lombok.*;
import org.project.dev.constrant.Role;
import org.project.dev.member.entity.SemiMemberEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SemiMemberDto {

    private Long semiMemberId;

    @NotBlank
    @Size(min = 10, max = 255)
    private String semiMemberEmail;

    @NotBlank
    @Size(min = 10, max = 11)
    @Pattern(regexp = "(01[016789])(\\d{3,4})(\\d{4})", message = "올바른 휴대전화번호를 입력해주세요")
    private String semiMemberPhone;

    private Role role;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    public static SemiMemberDto toSemiMemberDto(SemiMemberEntity semiMemberEntity) {

        SemiMemberDto semiMemberDto=new SemiMemberDto();

        semiMemberDto.setSemiMemberId(semiMemberEntity.getSemiMemberId());
        semiMemberDto.setSemiMemberEmail(semiMemberEntity.getSemiMemberEmail());
        semiMemberDto.setSemiMemberPhone(semiMemberEntity.getSemiMemberPhone());
        semiMemberDto.setRole(semiMemberEntity.getRole());
        semiMemberDto.setCreateTime(semiMemberEntity.getCreateTime());
        semiMemberDto.setUpdateTime(semiMemberEntity.getUpdateTime());
        return semiMemberDto;
    }

}
