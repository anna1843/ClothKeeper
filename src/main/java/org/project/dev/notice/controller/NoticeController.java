package org.project.dev.notice.controller;


import lombok.RequiredArgsConstructor;
import org.project.dev.config.member.MyUserDetails;
import org.project.dev.member.dto.MemberDto;
import org.project.dev.member.entity.MemberEntity;
import org.project.dev.member.service.ImageServiceImpl;
import org.project.dev.member.service.MemberService;
import org.project.dev.notice.dto.NoticeDto;
import org.project.dev.notice.service.NoticeService;
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

@Controller
@RequestMapping("/notice")
@RequiredArgsConstructor
public class NoticeController {

    @Autowired
    private final NoticeService noticeService;
    private final MemberService memberService;
    private final ImageServiceImpl imageService;

    /*
    Todo
     1. rladpwls1843@gamil.com
     2. 공지사항 쓰기페이지로 이동
     3.
     4.
     */
    @GetMapping("/write")
    public String getNoticeWrite(NoticeDto noticeDto, @AuthenticationPrincipal MyUserDetails myUserDetails, Model model){

        MemberDto member = memberService.detailMember(myUserDetails.getMemberEntity().getMemberId());
        String memberImageUrl = imageService.findImage(member.getMemberEmail()).getImageUrl();

        model.addAttribute("member", member);
        model.addAttribute("memberImageUrl", memberImageUrl);

        return "notice/write";
    }
    @PostMapping("/write")
    public String postNoticeWrite(@Validated NoticeDto noticeDto, BindingResult bindingResult, Model model,
                                  @AuthenticationPrincipal MyUserDetails myUserDetails){
        if(bindingResult.hasErrors()){
            return "notice/write";
        }
        myUserDetails.getMemberEntity();
        int rs = noticeService.noticeInsert(noticeDto, myUserDetails);
        System.out.println(rs+ " rs ");
        if(rs==1){
            return "redirect:/notice/list?page=0&select=&search=";
        }
        return "index";
    }
    /*
    Todo
     1. rladpwls1843@gamil.com
     2. 공지사항 목록 페이지로 이동
     3. 공지사항 목록 & 페이징 & 검색
     4.
     */
//    @GetMapping("/list")
//    public String getNoticeList(
//            @PageableDefault(page = 0, size = 10, sort = "notId", direction = Sort.Direction.DESC) Pageable pageable,
//            Model model,
//            @RequestParam(required = false, value = "select") String noticeSelect,
//            @RequestParam(required = false, value = "search") String noticeSearch,
//            @AuthenticationPrincipal MyUserDetails myUserDetails
//            ){
//
//        myUserDetails.getMemberEntity();
//
//        MemberDto member = memberService.detailMember(myUserDetails.getMemberEntity().getMemberId());
//        String memberImageUrl = imageService.findImage(member.getMemberEmail()).getImageUrl();
//
//        Page<NoticeDto> noticeList = noticeService.noticeList(pageable, noticeSelect, noticeSearch, myUserDetails);
//        // select에 해당하는 종류에 search의 내용이 포함되어 있는지 검색 + 목록페이징 구현
//
//        Long totalCount = noticeList.getTotalElements();
//        int totalPage = noticeList.getTotalPages();
//        int pageSize = noticeList.getSize();
//        int nowPage = noticeList.getNumber();
//        int blockNum = 10;
//
//        int startPage = (int)((Math.floor(nowPage/blockNum)*blockNum) + 1 <= totalPage ?
//                (Math.floor(nowPage/blockNum)*blockNum) + 1 : totalPage);
//        int endPage = (startPage + blockNum - 1 < totalPage ? startPage + blockNum - 1 : totalPage);
//
//        if(!noticeList.isEmpty()) {
//            model.addAttribute("noticeList", noticeList);
//            model.addAttribute("startPage", startPage);
//            model.addAttribute("endPage", endPage);
//            model.addAttribute("myUserDetails", myUserDetails);
//
//            model.addAttribute("member", member);
//            model.addAttribute("memberImageUrl", memberImageUrl);
//
//            return "notice/list";
//        }
//
//        return "notice/list";
//    }
    @GetMapping("/list")
    public String getNoticeList(
            @PageableDefault(page = 0, size = 10, sort = "notId", direction = Sort.Direction.DESC) Pageable pageable,
            Model model,
            @RequestParam(required = false, value = "select") String noticeSelect,
            @RequestParam(required = false, value = "search") String noticeSearch,
            @AuthenticationPrincipal MyUserDetails myUserDetails
    ){
        if (myUserDetails != null) {
            myUserDetails.getMemberEntity();

            MemberDto member = memberService.detailMember(myUserDetails.getMemberEntity().getMemberId());
            String memberImageUrl = imageService.findImage(member.getMemberEmail()).getImageUrl();

            model.addAttribute("member", member);
            model.addAttribute("memberImageUrl", memberImageUrl);
        }

        Page<NoticeDto> noticeList = noticeService.noticeList(pageable, noticeSelect, noticeSearch);
        // select에 해당하는 종류에 search의 내용이 포함되어 있는지 검색 + 목록페이징 구현

        Long totalCount = noticeList.getTotalElements();
        int totalPage = noticeList.getTotalPages();
        int pageSize = noticeList.getSize();
        int nowPage = noticeList.getNumber();
        int blockNum = 10;

        int startPage = (int)((Math.floor(nowPage/blockNum)*blockNum) + 1 <= totalPage ?
                (Math.floor(nowPage/blockNum)*blockNum) + 1 : totalPage);
        int endPage = (startPage + blockNum - 1 < totalPage ? startPage + blockNum - 1 : totalPage);

        model.addAttribute("noticeList", noticeList);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("myUserDetails", myUserDetails);

        return "notice/list";
    }

    /*
       Todo
        1. rladpwls1843@gamil.com
        2. 공지사항 종류에 따른 목록 페이지로 이동
        3. html의 a태그에 설정되어 있는 type에 해당하는 list만 출력
        4.
       */
//    @GetMapping("/list/{type}")
//    public String getNoticeList(
//            @PageableDefault(page = 0, size = 10, sort = "notId",direction = Sort.Direction.DESC)Pageable pageable,
//            Model model,
//            @PathVariable("type") String type,
//            @AuthenticationPrincipal MyUserDetails myUserDetails){
//
//        myUserDetails.getMemberEntity();
//        MemberDto member = memberService.detailMember(myUserDetails.getMemberEntity().getMemberId());
//        String memberImageUrl = imageService.findImage(member.getMemberEmail()).getImageUrl();
//
//        // type을 가져오고 페이징
//        Page<NoticeDto> noticeList = noticeService.noticeList(type, pageable, myUserDetails);
//
//        if (noticeList==null) {
//            throw new RuntimeException("list none");
//        }
//        Long totalCount = noticeList.getTotalElements();
//        int totalPage = noticeList.getTotalPages();
//        int pageSize = noticeList.getSize();
//        int nowPage = noticeList.getNumber();
//        int blockNum = 10;
//
//        int startPage = (int)((Math.floor(nowPage/blockNum)*blockNum) + 1 <= totalPage ?
//                (Math.floor(nowPage/blockNum)*blockNum) + 1 : totalPage);
//        int endPage = (startPage + blockNum - 1 < totalPage ? startPage + blockNum - 1 : totalPage);
//
//        if(!noticeList.isEmpty()){
//            model.addAttribute("noticeList", noticeList);
//            model.addAttribute("startPage", startPage);
//            model.addAttribute("endPage", endPage);
//            model.addAttribute("member", member);
//            model.addAttribute("memberImageUrl", memberImageUrl);
//            return "notice/list";
//        }
//        System.out.println("조회할 공지사항이 없다.");
//
//        model.addAttribute("member", member);
//        model.addAttribute("memberImageUrl", memberImageUrl);
//
//        return "notice/list";
//    }
    @GetMapping("/list/{type}")
    public String getNoticeList(
            @PageableDefault(page = 0, size = 10, sort = "notId",direction = Sort.Direction.DESC)Pageable pageable,
            Model model,
            @PathVariable("type") String type,
            @AuthenticationPrincipal MyUserDetails myUserDetails){

        if (myUserDetails != null) {
            MemberDto member = memberService.detailMember(myUserDetails.getMemberEntity().getMemberId());
            String memberImageUrl = imageService.findImage(member.getMemberEmail()).getImageUrl();

            model.addAttribute("member", member);
            model.addAttribute("memberImageUrl", memberImageUrl);
        }

        // type을 가져오고 페이징
        Page<NoticeDto> noticeList = noticeService.noticeList(type, pageable);

        if (noticeList==null) {
            throw new RuntimeException("list none");
        }
        Long totalCount = noticeList.getTotalElements();
        int totalPage = noticeList.getTotalPages();
        int pageSize = noticeList.getSize();
        int nowPage = noticeList.getNumber();
        int blockNum = 10;

        int startPage = (int)((Math.floor(nowPage/blockNum)*blockNum) + 1 <= totalPage ?
                (Math.floor(nowPage/blockNum)*blockNum) + 1 : totalPage);
        int endPage = (startPage + blockNum - 1 < totalPage ? startPage + blockNum - 1 : totalPage);


        model.addAttribute("noticeList", noticeList);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "notice/list";

    }

    /*
       Todo
        1. rladpwls1843@gamil.com
        2. 공지사항 상세보기 페이지로 이동
        3. 해당 id에 해당하는 공지사항에 대한 상세보기 페이지로 이동
        4.
        */
    @GetMapping("/detail/{id}")
    public String getNoticeDetail(@PathVariable("id") Long id, Model model,
                                  @AuthenticationPrincipal MyUserDetails myUserDetails){

        if (myUserDetails != null) {
            MemberDto member = memberService.detailMember(myUserDetails.getMemberEntity().getMemberId());
            String memberImageUrl = imageService.findImage(member.getMemberEmail()).getImageUrl();

            model.addAttribute("member", member);
            model.addAttribute("memberImageUrl", memberImageUrl);
        }

        NoticeDto noticeDto = noticeService.noticeDetail(id);

        if(noticeDto != null){
            model.addAttribute("noticeDto", noticeDto);
            return "notice/detail";
        }
//        model.addAttribute("myUserDetails", myUserDetails);
        return "redirect:/notice/list?page=0&select=&search=t";
    }

    /* TODO
        1. rladpwls1843@gamil.com
        2. 공지사항 글 수정 페이지로 이동
        3.
        4.
    */
    @GetMapping("/update/{id}")
    public String getNoticeUpdate(@PathVariable("id") Long id, Model model,
                                  @AuthenticationPrincipal MyUserDetails myUserDetails){

        MemberDto member = memberService.detailMember(myUserDetails.getMemberEntity().getMemberId());
        String memberImageUrl = imageService.findImage(member.getMemberEmail()).getImageUrl();

        NoticeDto noticeDto = noticeService.noticeUpdate(id);

        if(noticeDto != null){
            model.addAttribute("noticeDto", noticeDto);
            model.addAttribute("member", member);
            model.addAttribute("memberImageUrl", memberImageUrl);
            return "notice/update";
        }
        return "redirect:/notice/list?page=0&select=&search=";
    }
    @PostMapping("/update/{id}")
    public String postNoticeUpdate(@PathVariable("id") Long id, NoticeDto noticeDto, Model model,
                                   @AuthenticationPrincipal MyUserDetails myUserDetails){

        MemberDto member = memberService.detailMember(myUserDetails.getMemberEntity().getMemberId());
        String memberImageUrl = imageService.findImage(member.getMemberEmail()).getImageUrl();

        NoticeDto noticeDto1 = noticeService.noticeUpdateOk(noticeDto, id);
        model.addAttribute("noticeDto", noticeDto1); // update한 값을 model객체에 받는다.

        model.addAttribute("member", member);
        model.addAttribute("memberImageUrl", memberImageUrl);

        return "notice/detail";
        // 기존의 redirec로 갈 경우 조회수가 증가하므로 html문으로 보낸다.

    }


   /* TODO
        1. rladpwls1843@gamil.com
        2. 공지사항 글 삭제
        3.
        4.
    */

    @GetMapping("/delete/{id}")
    public String getNoticeDelete(@PathVariable("id") Long id){
        int rs = noticeService.noticeDelete(id);
        if(rs == 1){
            System.out.println("공지사항 삭제");
        }else{
            System.out.println("공지사항 삭제 실패");
        }
        return "redirect:/notice/list?page=0&select=&search=";
    }

}