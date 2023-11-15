package org.project.dev.member.dto;

import lombok.*;
import org.project.dev.constrant.Role;
import org.project.dev.member.entity.MemberEntity;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MemberDto {

    private Long memberId;

    @NotBlank
    @Size(min = 10, max = 255)
    @Email
    private String memberEmail;

    // emailId, domailCustom 합쳐서 저장
    private String emailId;

    private String domainCustom;

    @NotBlank
    @Size(min = 8, max = 255)
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&^./,+=-])[A-Za-z\\d@$!%*#?&^./,+=-]{8,}$",
            message = "최소 하나의 특수문자, 알파벳, 숫자를 포함해야 합니다")
    private String memberPassword;

    private String confirmPassword;

    @NotBlank
    @Size(min = 2, max = 50)
//    @Pattern(regexp = "^[가-힣a-zA-Z]*$", message = "한글과 영문만 입력 가능합니다" )  // 자바스크립트로 처리
    private String memberName;

    @NotBlank
    @Size(min = 2, max = 15)
    private String memberNickName;

    @NotBlank
    @Size(min = 10, max = 11)
//    @Pattern(regexp = "(01[016789])(\\d{3,4})(\\d{4})", message = "올바른 휴대전화번호를 입력해주세요") 자바스크립트로 처리
    private String memberPhone;

//    @NotBlank
//    @Size(min = 8, max = 8)
//    private String memberBirth;

    // 생년월일(birthYear, birthMonth, birthDay 합쳐서 저장)
    private String memberBirth;

    private int birthYear;

    private int birthMonth;

    private int birthDay;

    @NotBlank(message = "우편번호를 입력해주세요")
    private String memberPostCode;

    @NotBlank(message = "도로명주소를 입력해주세요")
    private String memberStreetAddress;

    @NotBlank(message = "상세주소를 입력해주세요")
    private String memberDetailAddress;

    private Role role;

    // 비밀번호 찾기 기능에서 이메일, 휴대전화번호 일치하는지 확인 시 필요 //
    private boolean matching;
    public MemberDto(boolean matching) {
        this.matching = matching;
    }

    public boolean isMatching(){ // 부에서 해당 멤버 변수 값을 읽을 수 있는 인터페이스를 제공하기 위해 사용
        return matching;
    }

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private String imageUrl;

    public static MemberDto toMemberDto(MemberEntity memberEntity) {
            MemberDto memberDto=new MemberDto();
            memberDto.setMemberId(memberEntity.getMemberId());
            memberDto.setMemberEmail(memberEntity.getMemberEmail());
            memberDto.setMemberPassword(memberEntity.getMemberPassword());
            memberDto.setMemberName(memberEntity.getMemberName());
            memberDto.setMemberNickName(memberEntity.getMemberNickName());
            memberDto.setMemberPhone(memberEntity.getMemberPhone());
            memberDto.setMemberBirth(memberEntity.getMemberBirth());
            memberDto.setMemberPostCode(memberEntity.getMemberPostCode());
            memberDto.setMemberStreetAddress(memberEntity.getMemberStreetAddress());
            memberDto.setMemberDetailAddress(memberEntity.getMemberDetailAddress());
            memberDto.setRole(memberEntity.getRole());
            memberDto.setCreateTime(memberEntity.getCreateTime());
            memberDto.setUpdateTime(memberEntity.getUpdateTime());

            memberDto.setImageUrl(memberEntity.getImage().getImageUrl());

            return memberDto;
    }



}
