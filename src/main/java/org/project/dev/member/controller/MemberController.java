package org.project.dev.member.controller;

import lombok.RequiredArgsConstructor;
import org.hibernate.dialect.MySQL5Dialect;
import org.project.dev.config.member.MyUserDetails;
import org.project.dev.member.dto.MemberDto;
import org.project.dev.member.repository.MemberRepository;
import org.project.dev.member.service.ImageService;
import org.project.dev.member.service.MemberService;
import org.project.dev.member.service.SemiMemberService;
import org.project.dev.notice.dto.InquiryDto;
import org.project.dev.notice.entity.InquiryEntity;
import org.project.dev.notice.service.InquiryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final ImageService imageService;
    private final InquiryService inquiryService;

    // Create
    @GetMapping("/join")
    public String getJoin(MemberDto memberDto, Model model){

        // 연도, 월, 일 데이터를 모델에 추가하여 뷰로 전달
        List<Integer> birthYears = new ArrayList<>();
        for (int year = 2023; year >= 1900; year--) { // 2023부터 1900까지 역순으로 추가
            birthYears.add(year);
        }
        List<Integer> birthMonths = new ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            birthMonths.add(month);
        }
        List<Integer> birthDays = new ArrayList<>();
        for (int day = 1; day <= 31; day++) {
            birthDays.add(day);
        }

        model.addAttribute("birthYears", birthYears);
        model.addAttribute("birthMonths", birthMonths);
        model.addAttribute("birthDays", birthDays);

        return "member/join";
    }

    @PostMapping("/join")
    public String postJoin(@Valid @ModelAttribute MemberDto memberDto, BindingResult bindingResult){

        if (bindingResult.hasErrors()) {
            return "member/join";
        }
        // 비밀번호 일치 확인
        if (!memberDto.getMemberPassword().equals(memberDto.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "error.confirmPassword", "비밀번호가 일치하지않습니다");
            return "member/join";
        }

        // 생년월일 정보를 조합하여 하나의 문자열로 만듭니다.
        String birthDate = String.format("%04d%02d%02d", memberDto.getBirthYear(), memberDto.getBirthMonth(), memberDto.getBirthDay());

        // MemberDto에 생년월일을 설정합니다.
        memberDto.setMemberBirth(birthDate);

        memberService.insertMember(memberDto);
        return "login";
    }

    // Login
    @GetMapping("/login")
    public String getLogin(@AuthenticationPrincipal MyUserDetails myUserDetails, Model model){

        if (myUserDetails != null) {
            MemberDto member = memberService.detailMember(myUserDetails.getMemberEntity().getMemberId());
            String memberImageUrl = imageService.findImage(member.getMemberEmail()).getImageUrl();
            model.addAttribute("memberImageUrl", memberImageUrl);
            model.addAttribute("member", member);
        }
        return "member/login";
    }

    // Read_paging - 회원 목록 조회
    @GetMapping("/pagingList")
    public String getMemberList(
            @PageableDefault(page=0, size=7, sort = "memberId", direction = Sort.Direction.DESC) Pageable pageable,
            Model model,
            @RequestParam(value = "subject", required = false) String subject,
            @RequestParam(value = "search", required = false) String search,
            @AuthenticationPrincipal MyUserDetails myUserDetails
    ) {

        if (myUserDetails != null) {
            MemberDto member = memberService.detailMember(myUserDetails.getMemberEntity().getMemberId());
            String memberImageUrl = imageService.findImage(member.getMemberEmail()).getImageUrl();

            model.addAttribute("member", member);
            model.addAttribute("memberImageUrl", memberImageUrl);
            model.addAttribute("myUserDetails", myUserDetails);
        }

        Page<MemberDto> memberList = memberService.memberList(pageable, subject, search);

        Long totalCount = memberList.getTotalElements();
        int totalPage = memberList.getTotalPages();
        int pageSize = memberList.getSize();
        int nowPage = memberList.getNumber();
        int blockNum = 10;

        int startPage = (int) ((Math.floor(nowPage / blockNum) * blockNum) + 1 <= totalPage ?
                (Math.floor(nowPage / blockNum) * blockNum) + 1 : totalPage);
        int endPage = (startPage + blockNum - 1 < totalPage ? startPage + blockNum - 1 : totalPage);

        model.addAttribute("memberList", memberList);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "member/pagingList"; // 이름 바꿔주기(memberList 지우고 memberList로)
    }

    // Detail - 회원 상세 보기
    @GetMapping("/detail/{memberId}")
    public String getDetail(@PathVariable("memberId") Long memberId, Model model){

        MemberDto member=memberService.detailMember(memberId);
        // 이미지 url을 db에서 가져오기
        String memberImageUrl = imageService.findImage(member.getMemberEmail()).getImageUrl();

        model.addAttribute("member", member);
//        model.addAttribute("myUserDetails", myUserDetails);
        model.addAttribute("memberImageUrl", memberImageUrl); // 이미지 url 모델에 추가

        return "member/detail";
    }

    // Update - 회원 수정 화면
    @GetMapping("/update/{memberId}")
    public String getUpdate(@PathVariable("memberId") Long memberId, MemberDto memberDto, Model model, @AuthenticationPrincipal MyUserDetails myUserDetails){

        // 연도, 월, 일 데이터를 모델에 추가하여 뷰로 전달
        List<Integer> birthYears = new ArrayList<>();
        for (int year = 2023; year >= 1900; year--) { // 2023부터 1900까지 역순으로 추가
            birthYears.add(year);
        }
        List<Integer> birthMonths = new ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            birthMonths.add(month);
        }
        List<Integer> birthDays = new ArrayList<>();
        for (int day = 1; day <= 31; day++) {
            birthDays.add(day);
        }

        if (myUserDetails != null) {
            MemberDto member = memberService.detailMember(memberId);
            String memberImageUrl = imageService.findImage(member.getMemberEmail()).getImageUrl();

            model.addAttribute("member", member);
            model.addAttribute("memberImageUrl", memberImageUrl);
        }

        model.addAttribute("birthYears", birthYears);
        model.addAttribute("birthMonths", birthMonths);
        model.addAttribute("birthDays", birthDays);

        memberDto = memberService.updateViewMember(memberId);
        model.addAttribute("memberDto", memberDto);

        System.out.println("imageUrl : " + memberDto.getImageUrl()); // 오류 발견 목적으로 써놓음, 지워도 됨

        return "member/update";
    }

    // Update - 실제 실행
    @PostMapping("/update")
    public String postUpdate(@Valid MemberDto memberDto, BindingResult bindingResult, @AuthenticationPrincipal MyUserDetails myUserDetails) {

        if (bindingResult.hasErrors()) {
            System.out.println("유효성 검증 관련 오류 발생");
            return "redirect:/";
        }

        // 생년월일 정보를 조합하여 하나의 문자열로 만듭니다.
        String birthDate = String.format("%04d%02d%02d", memberDto.getBirthYear(), memberDto.getBirthMonth(), memberDto.getBirthDay());

        // MemberDto에 생년월일을 설정합니다.
        memberDto.setMemberBirth(birthDate);

        int rs = memberService.updateMember(memberDto);

        if (rs == 1) {
            System.out.println("회원정보 수정 성공");

            System.out.println("imageUrl : " + memberDto.getImageUrl()); // 오류 발견 목적으로 써놓음, 지워도 됨

            return "redirect:/member/detail/" + memberDto.getMemberId(); // 수정된 정보를 보여주는 상세 페이지로 이동
        } else {
            System.out.println("회원정보 수정 실패");
            return "redirect:/";
        }
    }

    // Delete - 회원 정보 삭제
    @GetMapping("/delete/{memberId}")
    public String getDelete(@PathVariable("memberId") Long memberId){

        int rs=memberService.deleteMember(memberId);

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

    // 정보 수정 전 비밀번호 확인 - 입력 화면
    @GetMapping("/confirmPassword/{memberId}")
    public String getConfirmPasswordView(@PathVariable("memberId") Long memberId, Model model, @AuthenticationPrincipal MyUserDetails myUserDetails){

        MemberDto member = memberService.detailMember(memberId);
        String memberImageUrl = imageService.findImage(member.getMemberEmail()).getImageUrl();

        model.addAttribute("memberId", memberId);
        model.addAttribute("member", member);
        model.addAttribute("memberImageUrl", memberImageUrl);

        return "member/confirmPassword";
    }

    // 입력한 현재비밀번호와 DB에 있는 현재비밀번호 일치하는지
    @PostMapping("/checkCurrentPassword")
    @ResponseBody
    public Map<String, Boolean> postCheckCurrentPassword(@RequestParam("currentPassword") String currentPassword,
                                                         @RequestParam("memberId") Long memberId) {

        boolean valid = memberService.checkCurrentPassword(memberId, currentPassword);

        Map<String, Boolean> response = new HashMap<>();
        response.put("valid", valid);
        return response;
    }

    @PostMapping("/confirmPassword")
    public String postConfirmPassword(@RequestParam("currentPassword") String currentPassword,
                                      @RequestParam("memberId") Long memberId,
                                      MemberDto memberDto){

        boolean valid=memberService.checkCurrentPassword(memberId, currentPassword);

        if (valid) {
            return "redirect:/member/changePassword/" + memberDto.getMemberId(); // 비밀번호 수정 페이지로 이동
        } else {
            return "redirect:/member/confirmPassword/" + memberDto.getMemberId(); // 다시 비밀번호 확인 페이지로 이동
        }
    }

    // 비밀번호 변경 페이지
    @GetMapping("/changePassword/{memberId}")
    public String getChangePasswordPage(@PathVariable("memberId") Long memberId, Model model, @AuthenticationPrincipal MyUserDetails myUserDetails) {

        MemberDto member = memberService.detailMember(memberId);
        String memberImageUrl = imageService.findImage(member.getMemberEmail()).getImageUrl();

        model.addAttribute("memberId", memberId);
        model.addAttribute("member", member);
        model.addAttribute("memberImageUrl", memberImageUrl);

        return "member/changePassword"; // changePassword.html 페이지로 이동
    }

    // 비밀번호 변경 실행
    @PostMapping("/changePassword")
    public String postChangePassword(@RequestParam("memberId") Long memberId,
                                     @RequestParam("currentPassword") String currentPassword,
                                     @RequestParam("newPassword") String newPassword,
                                     @RequestParam("confirmNewPassword") String confirmNewPassword,
                                     MemberDto memberDto, Model model) {

        // 새로운 비밀번호 확인과 일치하는지 검사
        if (!newPassword.equals(confirmNewPassword)) {
            // 일치하지 않을 때 처리 (예: 오류 메시지 표시)
            return "redirect:/member/changePassword"; // 비밀번호 변경 페이지로 다시 이동
        }

        // 비밀번호 변경 메서드 호출
        boolean success = memberService.changePassword(memberId, currentPassword, newPassword, memberDto);

        if (success) {
            // 비밀번호 변경 성공 시 처리 (예: 메시지 표시)
            System.out.println("비밀번호 변경 성공");
            return "redirect:/member/detail/" + memberDto.getMemberId(); // 수정된 정보를 보여주는 상세 페이지로 이동
        } else {
            // 비밀번호 변경 실패 시 처리 (예: 오류 메시지 표시)
            System.out.println("비밀번호 변경 실패");
            return "redirect:/member/changePassword"; // 비밀번호 변경 페이지로 다시 이동
        }
    }

    // 회원(소셜로그인 사용자 포함) 탈퇴 전 이메일 인증 확인 - 입력 화면
    @GetMapping("/confirmEmail/{memberId}")
    public String getConfirmEmailView(@PathVariable("memberId") Long memberId,
                                      @AuthenticationPrincipal MyUserDetails myUserDetails,
                                      Model model){

        MemberDto member = memberService.detailMember(memberId);
        String memberImageUrl = imageService.findImage(member.getMemberEmail()).getImageUrl();

        model.addAttribute("member", member);
        model.addAttribute("myUserDetails", myUserDetails);
        model.addAttribute("memberImageUrl", memberImageUrl);

        return "member/confirmEmail";
    }

    // 프로필 이미지 변경 페이지
    @GetMapping("/updateImage/{memberId}")
    public String getUpdateImage(@PathVariable("memberId") Long memberId, Model model){

        MemberDto member=memberService.detailMember(memberId);

        // 이미지 url을 db에서 가져오기
        String memberImageUrl = imageService.findImage(member.getMemberEmail()).getImageUrl();

        model.addAttribute("member", member);
        model.addAttribute("memberImageUrl", memberImageUrl); // 이미지 url 모델에 추가

        return "member/updateImage";
    }

    // 내가 작성한 문의사항만 출력
    @GetMapping("/inquiry")
    public String getMyInquiryList(
            @PageableDefault(page=0, size=10, sort = "inqId", direction = Sort.Direction.DESC) Pageable pageable,
            Model model,
            @RequestParam(value = "select", required = false) String inquirySelect,
            @RequestParam(value = "search", required = false) String inquirySearch,
            @AuthenticationPrincipal MyUserDetails myUserDetails) {

        if (myUserDetails != null) {
            MemberDto member = memberService.detailMember(myUserDetails.getMemberEntity().getMemberId());
            String memberImageUrl = imageService.findImage(member.getMemberEmail()).getImageUrl();

            model.addAttribute("member", member);
            model.addAttribute("memberImageUrl", memberImageUrl);
        }

        Page<InquiryDto> inquiryList = inquiryService.myInquiryList(pageable, inquirySelect, inquirySearch, myUserDetails.getMemberEntity());

        Long totalCount = inquiryList.getTotalElements();
        int totalPage = inquiryList.getTotalPages();
        int pageSize = inquiryList.getSize();
        int nowPage = inquiryList.getNumber();
        int blockNum = 10;

        int startPage = (int) ((Math.floor(nowPage / blockNum) * blockNum) + 1 <= totalPage ?
                (Math.floor(nowPage / blockNum) * blockNum) + 1 : totalPage);
        int endPage = (startPage + blockNum - 1 < totalPage ? startPage + blockNum - 1 : totalPage);

        model.addAttribute("inquiryList", inquiryList);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "member/myInquiryList";
    }

}
