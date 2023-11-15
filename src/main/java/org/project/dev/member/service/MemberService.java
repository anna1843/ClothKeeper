package org.project.dev.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.dev.cart.entity.CartEntity;
import org.project.dev.cart.repository.CartRepository;
import org.project.dev.member.dto.MemberDto;
import org.project.dev.member.entity.ImageEntity;
import org.project.dev.member.entity.MemberEntity;
import org.project.dev.member.repository.ImageRepository;
import org.project.dev.member.repository.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j // log 객체 사용하기위함
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;
    private final ImageRepository imageRepository;

    // Create + 장바구니 생성
    @Transactional
    public void insertMember(MemberDto memberDto) {

        MemberEntity memberEntity = MemberEntity.toMemberEntityInsert(memberDto, passwordEncoder);
        Long memberId = memberRepository.save(memberEntity).getMemberId();

        // 이미지 생성 및 저장
        ImageEntity imageEntity = new ImageEntity();
        imageEntity.setImageUrl("/profileImages/default.png");
        imageEntity.setMember(memberEntity);

        // ImageEntity를 db에 저장
        imageRepository.save(imageEntity);

        // 회원가입 이후 장바구니 생성
        createCartForMember(memberId);
    }

    // 장바구니 생성 메서드
    private void createCartForMember(Long memberId) {
        MemberEntity memberEntity = memberRepository.findById(memberId).orElseThrow(IllegalAccessError::new);
        CartEntity cart = CartEntity.createCart(memberEntity);
        cartRepository.save(cart);
    }

    // Read (to메서드)
    public List<MemberDto> listMember() {

        List<MemberDto> memberDtoList = new ArrayList<>();
        List<MemberEntity> memberEntityList = memberRepository.findAll();

        if (!memberEntityList.isEmpty()) {
            for (MemberEntity memberEntity : memberEntityList) {

                MemberDto memberDto = MemberDto.toMemberDto(memberEntity);
                memberDtoList.add(memberDto);
            }
        }
        return memberDtoList;
    }

    // Read, 페이징/검색
    public Page<MemberDto> memberList(Pageable pageable, String subject, String search) {

        Page<MemberEntity> memberEntities = null; // 기본 null값으로 설정

        if(subject.equals("memberEmail")){
            memberEntities = memberRepository.findByMemberEmailContaining(pageable, search);
        }else if(subject.equals("memberName")){
            memberEntities = memberRepository.findByMemberNameContaining(pageable, search);
        }else if(subject.equals("memberNickName")){
            memberEntities = memberRepository.findByMemberNickNameContaining(pageable, search);
        }else if(subject.equals("memberPhone")) {
            memberEntities = memberRepository.findByMemberPhoneContaining(pageable, search);
        }else if(subject.equals("memberBirth")) {
            memberEntities = memberRepository.findByMemberBirthContaining(pageable, search);
        }else if(subject.equals("memberStreetAddress")) {
            memberEntities = memberRepository.findByMemberStreetAddressContaining(pageable, search);
        }else if(subject.equals("memberDetailAddress")) {
            memberEntities = memberRepository.findByMemberDetailAddressContaining(pageable, search);
        }else{
            memberEntities = memberRepository.findAll(pageable);
        }

        memberEntities.getNumber();
        memberEntities.getTotalElements();
        memberEntities.getTotalPages();
        memberEntities.getSize();

        Page<MemberDto> memberDtos = memberEntities.map(MemberDto::toMemberDto);


        return memberDtos;
    }

    //  Detail (to메서드)
    public MemberDto detailMember(Long memberId) {

        Optional<MemberEntity> optionalMemberEntity = Optional.ofNullable(memberRepository.findById(memberId).orElseThrow(() -> {
            return new IllegalArgumentException("조회할 아이디가 없습니다");
        }));

        if (optionalMemberEntity.isPresent()) {

            ImageEntity imageEntity = optionalMemberEntity.get().getImage();

            return MemberDto.toMemberDto(optionalMemberEntity.get());
        }
        return null;
    }

    // Update - 수정 화면
    public MemberDto updateViewMember(Long memberId) {

        MemberEntity memberEntity = memberRepository.findById(memberId).orElseThrow(IllegalAccessError::new);
        return MemberDto.toMemberDto(memberEntity);
    }

    // Update - 실제 실행(to메서드)
    @Transactional
    public int updateMember(MemberDto memberDto) {

        Optional<MemberEntity> optionalMemberEntity = Optional.ofNullable(memberRepository.findById(memberDto.getMemberId()).orElseThrow(() -> {
            return new IllegalArgumentException("수정할 아이디가 없습니다");
        }));

        MemberEntity memberEntity = MemberEntity.toMemberEntityUpdate(memberDto);
        Long memberId = memberRepository.save(memberEntity).getMemberId();

        Optional<MemberEntity> optionalMemberEntity1 = Optional.ofNullable(memberRepository.findById(memberId).orElseThrow(() -> {
            return new IllegalArgumentException("수정할 아이디가 없습니다");
        }));

        if (optionalMemberEntity1.isPresent()) {
            System.out.println("회원정보 수정 성공");
            return 1;

        } else {
            System.out.println("회원정보 수정 실패");
            return 0;
        }
    }

    // Delete
    @Transactional
    public int deleteMember(Long memberId) {

        Optional<MemberEntity> optionalMemberEntity = Optional.ofNullable(memberRepository.findById(memberId).orElseThrow(() -> {
            return new IllegalArgumentException("삭제할 아이디가 없습니다");
        }));

        memberRepository.delete(optionalMemberEntity.get());

        Optional<MemberEntity> optionalMemberEntity1 = memberRepository.findById(memberId);

        if (!optionalMemberEntity1.isPresent()) {
            return 1;
        } else {
            return 0;
        }
    }

    // 이메일 중복 확인 메서드, CheckDuplicateController에서 두 테이블 직접 조회해 사용하면 이 메서드 필요 없음
//    @Transactional
//    public boolean existsByMemberEmail(String memberEmail) {
//        return memberRepository.existsByMemberEmail(memberEmail);
//    }

    // 닉네임 중복 확인 메서드
    @Transactional
    public boolean existsByMemberNickName(String memberNickName) {
        return memberRepository.existsByMemberNickName(memberNickName);
    }

    // 휴대전화번호 중복 확인 메서드, CheckDuplicateController에서 두 테이블 직접 조회해 사용하면 이 메서드 필요 없음
//    @Transactional
//    public boolean existsByMemberPhone(String memberPhone) {
//        return memberRepository.existsByMemberPhone(memberPhone);
//    }

    // 닉네임, 휴대전화번호 이용한 이메일 찾기 메서드
    public String findEmailByNicknameAndPhone(String memberNickName, String memberPhone) {

        MemberEntity memberEntity = memberRepository.findByMemberNickNameAndMemberPhone(memberNickName, memberPhone);
        if (memberEntity != null) {
            return memberEntity.getMemberEmail();
        }
        return null;
    }

    // 비밀번호 찾기 기능에서 이메일, 휴대전화번호 일치하는지 확인
    public boolean checkEmailPhoneMatching(String memberEmail, String memberPhone) {

        MemberEntity memberEntity = memberRepository.findByMemberEmailAndMemberPhone(memberEmail, memberPhone);
        return memberEntity != null; // 이메일과 휴대전화번호가 일치하면 memberEntity 객체가 반환되어서 null이 아님
    }

    // 임시 비밀번호 발급 관련 메서드
    public void SetTempPassword(String memberEmail, String tempPassword) {

        Optional<MemberEntity> optionalMemberEntity = memberRepository.findByMemberEmail(memberEmail);

        if (optionalMemberEntity.isPresent()) {
            MemberEntity memberEntity = optionalMemberEntity.get();
            String encodedPassword = passwordEncoder.encode(tempPassword);
            memberEntity.setMemberPassword(encodedPassword);
            memberRepository.save(memberEntity);
            log.info("이메일 주소에 임시 비밀번호가 설정되었습니다: {}", memberEmail);
        } else {
            log.error("해당 이메일을 가진 회원을 찾을수없습니다: {}", memberEmail);
        }
    }

    // 비밀번호 변경 전 현재 비밀번호 확인
    // 입력한 현재비밀번호와 DB에 있는 현재비밀번호 일치하는지 확인하는 메서드
    public boolean checkCurrentPassword(Long memberId, String currentPassword) {
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findById(memberId);

        if (optionalMemberEntity.isPresent()) {
            MemberEntity memberEntity = optionalMemberEntity.get();
            return passwordEncoder.matches(currentPassword, memberEntity.getMemberPassword());
        }
        return false;
    }

    // 비밀번호 변경 메서드
    public boolean changePassword(Long memberId, String currentPassword, String newPassword, MemberDto memberDto) {
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findById(memberId);

        if (optionalMemberEntity.isEmpty()) {
            throw new RuntimeException("해당 아이디를 가진 회원을 찾을 수 없습니다.");
        }

        MemberEntity memberEntity = optionalMemberEntity.get();

        // 현재 비밀번호 확인
        if (!passwordEncoder.matches(currentPassword, memberEntity.getMemberPassword())) {
            throw new RuntimeException("현재 비밀번호가 일치하지 않습니다.");
        }

        // 새로운 비밀번호 암호화
        String encryptedNewPassword = passwordEncoder.encode(newPassword);
        memberEntity.setMemberPassword(encryptedNewPassword);

        // 회원 정보 저장
        memberRepository.save(memberEntity);

        return true;
    }

    // memberId로 member 찾기
    public MemberEntity findMember(Long memberId) {
        return memberRepository.findById(memberId).get();
    }

    // 휴대전화번호 db에 저장될때 해당 형식으로 저장되도록 하는 메서드
    public void savePhoneNumber(MemberDto memberDto) {
        // 휴대전화번호 데이터 형식 변환
        String formattedPhoneNumber = transformPhoneNumber(memberDto.getMemberPhone());

        // JPA Entity 생성
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setMemberPhone(formattedPhoneNumber);
        memberRepository.save(memberEntity);
    }

    // 휴대전화번호 db에 저장될때 해당 형식으로 저장되도록 하는 메서드
    private String transformPhoneNumber(String memberPhone) {
        // 입력된 휴대전화번호에서 "-" 문자를 제거
        String cleanedPhoneNumber = memberPhone.replaceAll("-", "");

        return cleanedPhoneNumber;
    }


}





