package org.project.dev.member.controller;

import lombok.RequiredArgsConstructor;
import org.project.dev.config.member.MyUserDetails;
import org.project.dev.member.dto.ImageUploadDto;
import org.project.dev.member.entity.MemberEntity;
import org.project.dev.member.repository.ImageRepository;
import org.project.dev.member.service.ImageServiceImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/image")
public class ImageController {

    private final ImageServiceImpl imageService;

    // 이미지 등록
    @PostMapping("/upload")
    public String upload(@ModelAttribute ImageUploadDto imageUploadDto, @AuthenticationPrincipal MyUserDetails myUserDetails, Model model){

        MemberEntity member = myUserDetails.getMemberEntity();
        imageService.upload(imageUploadDto, member.getMemberEmail());

        // 이미지 URL을 모델에 추가
        model.addAttribute("memberImageUrl", "/profileImages/" + imageUploadDto.getFile().getOriginalFilename());

        return "redirect:/member/detail/" + myUserDetails.getMemberEntity().getMemberId();
    }

    // 이미지 삭제
    @PostMapping("/delete")
    public String deleteImage(@AuthenticationPrincipal MyUserDetails myUserDetails) {

        String memberEmail = myUserDetails.getUsername();
        imageService.deleteImage(memberEmail);
        return "redirect:/member/detail/" + myUserDetails.getMemberEntity().getMemberId();
    }

}
