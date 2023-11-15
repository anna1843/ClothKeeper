package org.project.dev.member.service;

import lombok.RequiredArgsConstructor;
import org.project.dev.cart.entity.CartEntity;
import org.project.dev.cart.repository.CartRepository;
import org.project.dev.member.dto.SemiMemberDto;
import org.project.dev.member.entity.SemiMemberEntity;
import org.project.dev.member.repository.SemiMemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SemiMemberService {

    private final SemiMemberRepository semiMemberRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;

    // Create + 장바구니 생성
    @Transactional
    public void insertSemiMember(SemiMemberDto semiMemberDto){

        SemiMemberEntity semiMemberEntity=SemiMemberEntity.toSemiMemberEntityInsert(semiMemberDto, passwordEncoder);
        Long memberId=semiMemberRepository.save(semiMemberEntity).getSemiMemberId();

        // 회원가입 이후 카트 생성
        createCartForMember(memberId);
    }
    private void createCartForMember(Long semiMemberId) {
        SemiMemberEntity semiMemberEntity = semiMemberRepository.findById(semiMemberId).orElseThrow(IllegalAccessError::new);
        CartEntity cart = CartEntity.createSemiCart(semiMemberEntity);
        cartRepository.save(cart);
    }

    // 이메일 중복 확인 메서드
    @Transactional
    public boolean existsBySemiMemberEmail(String semiMemberEmail) {
        return semiMemberRepository.existsBySemiMemberEmail(semiMemberEmail);
    }

    // Read - 간편회원목록
    public List<SemiMemberDto> listSemiMember(){

        List<SemiMemberDto> semiMemberDtoList=new ArrayList<>();
        List<SemiMemberEntity> semiMemberEntityList=semiMemberRepository.findAll();

        if (!semiMemberEntityList.isEmpty()) {
            for(SemiMemberEntity semiMemberEntity: semiMemberEntityList){

                SemiMemberDto semiMemberDto=SemiMemberDto.toSemiMemberDto(semiMemberEntity);
                semiMemberDtoList.add(semiMemberDto);
            }
        }
        return semiMemberDtoList;
    }

    //  Detail
    public SemiMemberDto detailSemiMember(Long semiMemberId){

        Optional<SemiMemberEntity> optionalSemiMemberEntity=Optional.ofNullable(semiMemberRepository.findById(semiMemberId).orElseThrow(()->{
            return new IllegalArgumentException("조회할 아이디가 없습니다");
        }));

        if (optionalSemiMemberEntity.isPresent()) {

            return SemiMemberDto.toSemiMemberDto(optionalSemiMemberEntity.get());
        }
        return null;
    }

    // Delete
    public int deleteSemiMember(Long semiMemberId) {

        Optional<SemiMemberEntity> optionalSemiMemberEntity=Optional.ofNullable(semiMemberRepository.findById(semiMemberId).orElseThrow(()->{
            return new IllegalArgumentException("삭제할 아이디가 없습니다");
        }));

        semiMemberRepository.delete(optionalSemiMemberEntity.get());

        Optional<SemiMemberEntity> optionalSemiMemberEntity1=semiMemberRepository.findById(semiMemberId);

        if(!optionalSemiMemberEntity1.isPresent()){
            return 1;
        }else{
            return 0;
        }
    }

    public SemiMemberEntity findSemiMember(Long semiMemberId) {
        return semiMemberRepository.findById(semiMemberId).get();
    }

    // 간편회원목록 페이징, 검색
    public Page<SemiMemberDto> semiMemberList(Pageable pageable, String subject, String search) {

        Page<SemiMemberEntity> semiMemberEntities = null; // 기본 null값으로 설정

        if(subject.equals("semiMemberEmail")){
            semiMemberEntities = semiMemberRepository.findBySemiMemberEmailContaining(pageable, search);
        }else{
            semiMemberEntities = semiMemberRepository.findAll(pageable);
        }

        semiMemberEntities.getNumber();
        semiMemberEntities.getTotalElements();
        semiMemberEntities.getTotalPages();
        semiMemberEntities.getSize();

        Page<SemiMemberDto> semiMemberDtos = semiMemberEntities.map(SemiMemberDto::toSemiMemberDto);


        return semiMemberDtos;
    }
}
