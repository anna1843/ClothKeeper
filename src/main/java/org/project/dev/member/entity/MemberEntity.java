package org.project.dev.member.entity;

import lombok.*;
import org.project.dev.cart.entity.CartEntity;
import org.project.dev.constrant.Role;
import org.project.dev.member.dto.MemberDto;
import org.project.dev.notice.entity.InquiryEntity;
import org.project.dev.product.entity.ProductEntity;
import org.project.dev.utils.BaseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "member_project")
public class MemberEntity extends BaseEntity {

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
    @Column(name = "member_phone", unique = true, nullable = false)
    private String memberPhone;

    // 생년월일
    @Column(name = "member_birth")
    private String memberBirth;

    // 우편번호(주소 api 이용 위해 필요)
    @Column(name = "member_postCode", nullable = false)
    private String memberPostCode;

    // 도로명주소(주소 api 이용 위해 필요)
    @Column(name = "member_streetAddress")
    private String memberStreetAddress;

    // 상세주소(주소 api 이용 위해 필요)
    @Column(name = "member_detailAddress")
    private String memberDetailAddress;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "temporary_password")
    private String temporaryPassword;

    // 연관 관계
    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private CartEntity cart;

    // 연관 관계
    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private ImageEntity image;

    // 연관 관계
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<ProductEntity> products = new ArrayList<>();

    // 연관 관계
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<InquiryEntity> inquiry = new ArrayList<>();

    public static MemberEntity toMemberEntityInsert(MemberDto memberDto, PasswordEncoder passwordEncoder) {

        MemberEntity memberEntity=new MemberEntity();

        memberEntity.setMemberEmail(memberDto.getMemberEmail());
        memberEntity.setMemberPassword(passwordEncoder.encode(memberDto.getMemberPassword()));
        memberEntity.setMemberName(memberDto.getMemberName());
        memberEntity.setMemberNickName(memberDto.getMemberNickName());
        memberEntity.setMemberPhone(memberDto.getMemberPhone());
        memberEntity.setMemberBirth(memberDto.getMemberBirth());
        memberEntity.setMemberPostCode(memberDto.getMemberPostCode());
        memberEntity.setMemberStreetAddress(memberDto.getMemberStreetAddress());
        memberEntity.setMemberDetailAddress(memberDto.getMemberDetailAddress());
        memberEntity.setRole(Role.ADMIN);

        return memberEntity;
    }
    public static MemberEntity toMemberEntityUpdate(MemberDto memberDto) {

        MemberEntity memberEntity=new MemberEntity();

        memberEntity.setMemberId(memberDto.getMemberId());
        memberEntity.setMemberEmail(memberDto.getMemberEmail());
        memberEntity.setMemberPassword(memberDto.getMemberPassword());
        memberEntity.setMemberName(memberDto.getMemberName());
        memberEntity.setMemberNickName(memberDto.getMemberNickName());
        memberEntity.setMemberPhone(memberDto.getMemberPhone());
        memberEntity.setMemberBirth(memberDto.getMemberBirth());
        memberEntity.setMemberPostCode(memberDto.getMemberPostCode());
        memberEntity.setMemberStreetAddress(memberDto.getMemberStreetAddress());
        memberEntity.setMemberDetailAddress(memberDto.getMemberDetailAddress());
        memberEntity.setRole(memberDto.getRole());
        memberEntity.setCreateTime(memberDto.getCreateTime());
        memberEntity.setUpdateTime(memberDto.getUpdateTime());

        return memberEntity;
    }
}
