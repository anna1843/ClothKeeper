package org.project.dev.notice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.dev.config.member.MyUserDetails;
import org.project.dev.config.semiMember.SemiMyUserDetails;
import org.project.dev.member.dto.MemberDto;
import org.project.dev.member.dto.SemiMemberDto;
import org.project.dev.member.entity.MemberEntity;
import org.project.dev.member.entity.SemiMemberEntity;
import org.project.dev.member.service.ImageServiceImpl;
import org.project.dev.member.service.MemberService;
import org.project.dev.member.service.SemiMemberService;
import org.project.dev.notice.dto.InquiryDto;
import org.project.dev.notice.service.InquiryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Slf4j // 송원철 / log에 출력 위한 용도
@Controller
@RequestMapping("/inquiry")
@RequiredArgsConstructor
public class InquiryController {

    @Autowired
    private final InquiryService inquiryService;
    private final MemberService memberService; // 송원철, 헤더에 nickName, image 가져오기 위한 용도
    private final ImageServiceImpl imageService; // 송원철, 헤더에 nickName, image 가져오기 위한 용도

    /*
   Todo
    1. rladpwls1843@gamil.com
    2. 문의사항 쓰기페이지로 이동
    3.
    4.
    */

    @GetMapping("/write")
    public String getInquiryWrite(InquiryDto inquiryDto, @AuthenticationPrincipal MyUserDetails myUserDetails,
                                  @AuthenticationPrincipal SemiMyUserDetails semiMyUserDetails, Model model){

        if(myUserDetails != null) {
            MemberDto member = memberService.detailMember(myUserDetails.getMemberEntity().getMemberId());
            String memberImageUrl = imageService.findImage(member.getMemberEmail()).getImageUrl();

            model.addAttribute("member", member);
            model.addAttribute("memberImageUrl", memberImageUrl);
        } else {
            log.info("member is null");
            log.info("semiMemberId : " + semiMyUserDetails.getSemiMemberEntity().getSemiMemberId());
        }

        return "inquiry/write";
    }
    @PostMapping("/write")
    public String postInquiryWrite(@Validated InquiryDto inquiryDto, BindingResult bindingResult, Model model,
                                   @AuthenticationPrincipal MyUserDetails myUserDetails) throws IOException {
        if(bindingResult.hasErrors()){
            return "inquiry/write";
        }
        MemberEntity member = myUserDetails.getMemberEntity(); // 현재 로그인한 사용자의 MemberEntity 가져오기

        int rs = inquiryService.InquiryInsert(inquiryDto, member);
        if(rs==1){
            return "redirect:/inquiry/list?page=0&select=&search=";
        }
        return "index";
    }

    /*
   Todo
    1. rladpwls1843@gamil.com
    2. 문의사항 목록 페이지로 이동
    3. 문의사항 목록 & 페이징 & 검색
    4.
    */
    @GetMapping("/list")
    public String getInquiryList(
            @PageableDefault(page=0, size=10, sort = "inqId", direction = Sort.Direction.DESC) Pageable pageable,
            Model model,
            @RequestParam(value = "select", required = false) String inquirySelect,
            @RequestParam(value = "search", required = false) String inquirySearch,
            @AuthenticationPrincipal MyUserDetails myUserDetails
    ) {
        if (myUserDetails != null) {
            MemberDto member = memberService.detailMember(myUserDetails.getMemberEntity().getMemberId());
            String memberImageUrl = imageService.findImage(member.getMemberEmail()).getImageUrl();

            model.addAttribute("member", member);
            model.addAttribute("memberImageUrl", memberImageUrl);
        }

        Page<InquiryDto> inquiryList = inquiryService.inquiryList(pageable, inquirySelect, inquirySearch);

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
        model.addAttribute("myUserDetails", myUserDetails);

        return "inquiry/list";
    }

    /*
           Todo
            1. rladpwls1843@gamil.com
            2. 문의사항 상세페이지로 이동 -> 해당 ID에 맞는
            3.
            4.
            */
    @GetMapping("/detail/{id}")
    public String getInquiryDetail(@PathVariable("id") Long id, Model model, @AuthenticationPrincipal MyUserDetails myUserDetails){

        MemberDto member = memberService.detailMember(myUserDetails.getMemberEntity().getMemberId());
        String memberImageUrl = imageService.findImage(member.getMemberEmail()).getImageUrl();

        InquiryDto inquiryDto = inquiryService.InquiryDetail(id);

        if(inquiryDto != null){
            model.addAttribute("inquiryDto", inquiryDto);
            model.addAttribute("member", member);
            model.addAttribute("memberImageUrl", memberImageUrl);
            return "inquiry/detail";
        }
        return "redirect:/inquiry/list?page=0&select=&search=";
    }

    /*
    TODO
    문의사항 수정하기 페이지로 이동
    */
    @GetMapping("/update/{id}")
    public String getInquiryUpdate(@PathVariable("id") Long id, Model model,
                                   @AuthenticationPrincipal MyUserDetails myUserDetails){
        myUserDetails.getMemberEntity();
        MemberDto member = memberService.detailMember(myUserDetails.getMemberEntity().getMemberId());
        String memberImageUrl = imageService.findImage(member.getMemberEmail()).getImageUrl();

        InquiryDto inquiryDto = inquiryService.InquiryUpdate(id);

        if(inquiryDto != null){
            model.addAttribute("inquiryDto",inquiryDto);
            model.addAttribute("member", member);
            model.addAttribute("memberImageUrl", memberImageUrl);
            return "inquiry/update";
        }
        model.addAttribute("member", member);
        model.addAttribute("memberImageUrl", memberImageUrl);

        return "redirect:/inquiry/list?page=0&select=&search=";
    }
    @PostMapping("/update/{id}")
    public String postInquiryUpdate(@PathVariable("id") Long id, InquiryDto inquiryDto, Model model,
                                    @AuthenticationPrincipal MyUserDetails myUserDetails){

        myUserDetails.getMemberEntity();
        MemberDto member = memberService.detailMember(myUserDetails.getMemberEntity().getMemberId());
        String memberImageUrl = imageService.findImage(member.getMemberEmail()).getImageUrl();

        InquiryDto inquiryDto1 = inquiryService.inquiryUpdateOk(inquiryDto,id);
        model.addAttribute("inquiryDto", inquiryDto1);

        model.addAttribute("member", member);
        model.addAttribute("memberImageUrl", memberImageUrl);

        return "inquiry/detail";
    }
    /*
    TODO
    문의사항 삭제하기 페이지로 이동
    */
    @GetMapping("/delete/{id}")
    public String getInquiryDelete(@PathVariable("id") Long id){
        int rs = inquiryService.InquiryDelete(id);
        if(rs == 1){
            System.out.println("문의사항 삭제");
        }else{
            System.out.println("문의사항 삭제 실패");
        }
        return "redirect:/inquiry/list?page=0&select=&search=";
    }
}
