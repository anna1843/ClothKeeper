package org.project.dev.review.controller;

import lombok.RequiredArgsConstructor;
import org.project.dev.config.member.MyUserDetails;
import org.project.dev.inquiryReply.dto.ReplyDto;
import org.project.dev.review.dto.ReviewDto;
import org.project.dev.review.service.ReviewService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/review")
@RestController
public class ReviewController {

    private final ReviewService reviewService;


    @PostMapping("/write")
    public @ResponseBody ReviewDto replyWrite(@ModelAttribute ReviewDto reviewDto,
                                              @AuthenticationPrincipal MyUserDetails myUserDetails){

        ReviewDto replyDto1 = reviewService.reviewWrite(reviewDto, myUserDetails);

        return  replyDto1;
    }

    @GetMapping("/list")
    public @ResponseBody List<ReviewDto> reviewList(@ModelAttribute ReviewDto reviewDto){
        List<ReviewDto> reviewDtos = reviewService.reviewList(reviewDto);
        return reviewDtos;
    }

    @PostMapping("/delete")
    public int reviewDelete(@ModelAttribute ReviewDto reviewDto){
        int rs = reviewService.replyDelete(reviewDto.getId());
        return rs;
    }

    @PostMapping("/up")
    public int reviewUp(@ModelAttribute ReviewDto reviewDto,
                       @AuthenticationPrincipal MyUserDetails myUserDetails){
        int rs = reviewService.reviewUp(reviewDto, myUserDetails);

        return rs;
    }


}
