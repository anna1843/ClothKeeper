package org.project.dev.member.controller;

import lombok.RequiredArgsConstructor;
import org.project.dev.config.member.MyUserDetails;
import org.project.dev.config.semiMember.SemiMyUserDetails;
import org.project.dev.member.dto.MemberDto;
import org.project.dev.member.dto.SemiMemberDto;
import org.project.dev.member.service.ImageServiceImpl;
import org.project.dev.member.service.MemberService;
import org.project.dev.member.service.SemiMemberService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/semiMember")
@RequiredArgsConstructor
public class SemiMemberController {

    private final SemiMemberService semiMemberService;
    private final MemberService memberService; // 프로필 이미지 가져오기위함
    private final ImageServiceImpl imageService; // 프로필 이미지 가져오기위함

    @GetMapping("/join")
    public String getJoin(SemiMemberDto semiMemberDto){

        return "semiMember/join";
    }

    @PostMapping("/join")
    public String postJoin(@Valid SemiMemberDto semiMemberDto, BindingResult bindingResult){

        if (bindingResult.hasErrors()) {
            return "semiMember/join";
        }

        semiMemberService.insertSemiMember(semiMemberDto);
        return "login";
    }

    @GetMapping("/login")
    public String getLogin(){
        return "semiMember/login";
    }


    // Read - 간편회원목록 조회
//    @GetMapping("/semiMemberList")
//    public String getSemiMemberList(@AuthenticationPrincipal MyUserDetails myUserDetails, SemiMyUserDetails semiMyUserDetails, Model model){
//        List<SemiMemberDto> semiMemberDtoList=semiMemberService.listSemiMember();
//
//        model.addAttribute("semiMemberDtoList", semiMemberDtoList);
//        model.addAttribute("semiMyUserDetails", semiMyUserDetails);
//        model.addAttribute("myUserDetails", myUserDetails);
//
//        return "semiMember/semiMemberList";
//    }

    // 간편회원 목록 / 페이징, 검색
    @GetMapping("/pagingList")
    public String getSemiMemberList(
            @PageableDefault(page=0, size=7, sort = "semiMemberId", direction = Sort.Direction.DESC) Pageable pageable,
            Model model,
            @RequestParam(value = "subject", required = false) String subject,
            @RequestParam(value = "search", required = false) String search,
            @AuthenticationPrincipal MyUserDetails myUserDetails
    ) {

        if(myUserDetails != null){
            MemberDto member = memberService.detailMember(myUserDetails.getMemberEntity().getMemberId());
            String memberImageUrl = imageService.findImage(member.getMemberEmail()).getImageUrl();

            model.addAttribute("member", member);
            model.addAttribute("memberImageUrl", memberImageUrl);
            model.addAttribute("myUserDetails", myUserDetails);
        }

        Page<SemiMemberDto> semiMemberList = semiMemberService.semiMemberList(pageable, subject, search);

        Long totalCount = semiMemberList.getTotalElements();
        int totalPage = semiMemberList.getTotalPages();
        int pageSize = semiMemberList.getSize();
        int nowPage = semiMemberList.getNumber();
        int blockNum = 10;

        int startPage = (int) ((Math.floor(nowPage / blockNum) * blockNum) + 1 <= totalPage ?
                (Math.floor(nowPage / blockNum) * blockNum) + 1 : totalPage);
        int endPage = (startPage + blockNum - 1 < totalPage ? startPage + blockNum - 1 : totalPage);

        model.addAttribute("semiMemberList", semiMemberList);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "semiMember/pagingList"; // 이름 바꿔주기(memberList 지우고 memberList로)
    }

    // Detail - 회원 상세 보기
    @GetMapping("/detail/{semiMemberId}")
    public String getDetail(@PathVariable("semiMemberId") Long semiMemberId, Model model,
                            @AuthenticationPrincipal SemiMyUserDetails semiMyUserDetails,
                            @AuthenticationPrincipal MyUserDetails myUserDetails){

        if (myUserDetails != null) {
            MemberDto member=memberService.detailMember(myUserDetails.getMemberEntity().getMemberId());
            model.addAttribute("member", member);
        }
        SemiMemberDto semiMember=semiMemberService.detailSemiMember(semiMemberId);

        model.addAttribute("semiMember", semiMember);
        model.addAttribute("semiMyUserDetails", semiMyUserDetails);

        return "semiMember/detail";
    }

    // Delete - 회원 정보 삭제
    @GetMapping("/delete/{semiMemberId}")
    public String getDelete(@PathVariable("semiMemberId") Long semiMemberId){

        int rs=semiMemberService.deleteSemiMember(semiMemberId);

        if (rs==1) {
            System.out.println("회원정보 삭제 성공");

            // 회원정보 삭제 후 로그아웃 처리
            Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
            if (authentication!=null) {
                SecurityContextHolder.clearContext();
            }
            return "redirect:/";

        }else{
            System.out.println("회원정보 삭제 실패");
            return "redirect:/";
        }
    }

    // 회원(소셜로그인 사용자 포함) 탈퇴 전 이메일 인증 확인 - 입력 화면
    @GetMapping("/confirmEmail/{semiMemberId}")
    public String getConfirmEmailView(@PathVariable("semiMemberId") Long semiMemberId,
                                      @AuthenticationPrincipal SemiMyUserDetails semiMyUserDetails,
                                      Model model){

        SemiMemberDto semiMember=semiMemberService.detailSemiMember(semiMemberId);

        model.addAttribute("semiMember", semiMember);
        model.addAttribute("semiMyUserDetails", semiMyUserDetails);

        return "semiMember/confirmEmail";
    }


}
