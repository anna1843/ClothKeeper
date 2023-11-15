package org.project.dev.controller;

import lombok.RequiredArgsConstructor;
import org.project.dev.config.member.MyUserDetails;
import org.project.dev.member.dto.MemberDto;
import org.project.dev.member.service.ImageServiceImpl;
import org.project.dev.member.service.MemberService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


/*

 */


@Controller
@RequiredArgsConstructor
//@RequestMapping("/")
public class HomeController {

    /*
    Todo
     1. 코드 작성자 (이메일 기재)
     2. 간략한 기능 설명
     3. 필수 데이터
     4. 기타
     */


     /*
    Todo
     1. songwc3@gmail.com
     2. 회원가입, 로그인 View
     3. 없음
     4. 없음
     */

    private final ImageServiceImpl imageService;
    private final MemberService memberService;

    // 프로필 이미지 가져옴
    @GetMapping({"","/index"})
    public String index(@AuthenticationPrincipal MyUserDetails myUserDetails, Model model) {

        if (myUserDetails != null) {
            MemberDto member=memberService.detailMember(myUserDetails.getMemberEntity().getMemberId());

            // 이미지 url을 db에서 가져오기
            String memberImageUrl = imageService.findImage(member.getMemberEmail()).getImageUrl();

            model.addAttribute("memberImageUrl", memberImageUrl); // 이미지 url 모델에 추가
            model.addAttribute("member", member);
        }
        return "index";
    }

    /*
    Todo
     1. catfather@49gamil.com
     2. 이 사이트를 접속하면 제일 처음 요청이 들어오는 컨트롤러입니다
     3. 없음
     4. 없음
     */
    @GetMapping("/kream")
    public String kream(){
        return "kream";
    }

    /*
    Todo
     1. songwc3@gmail.com
     2. 회원가입, 로그인 View
     3. 없음
     4. 없음
     */
    @GetMapping("/join")
    public String join(){
        return "join";
    }

    // 로그인 View(일반회원, 간편회원 포함)
    @GetMapping("/login")
    public String getLogin(){
        return "login";
    }
}
