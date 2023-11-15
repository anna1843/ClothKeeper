package org.project.dev.inquiryReply.controller;

import lombok.RequiredArgsConstructor;
import org.project.dev.config.member.MyUserDetails;
import org.project.dev.inquiryReply.dto.ReplyDto;
import org.project.dev.inquiryReply.service.ReplyService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/reply")
@RestController
public class ReplyController {

    private final ReplyService replyService;


    @PostMapping("/write")
    public @ResponseBody ReplyDto replyWrite(@ModelAttribute ReplyDto replyDto,
                                        @AuthenticationPrincipal MyUserDetails myUserDetails){

        ReplyDto replyDto1 = replyService.replyWrite(replyDto, myUserDetails);

        return  replyDto1;
    }

    @GetMapping("/list")
    public @ResponseBody List<ReplyDto> replyList(@ModelAttribute ReplyDto replyDto){
        List<ReplyDto> replyDtos = replyService.replyList(replyDto);
        return replyDtos;
    }

    @PostMapping("/delete")
    public int replyDelete(@ModelAttribute ReplyDto replyDto){
        int rs = replyService.replyDelete(replyDto.getId());
        return rs;
    }

    @PostMapping("/up")
    public int replyUp(@ModelAttribute ReplyDto replyDto,
                       @AuthenticationPrincipal MyUserDetails myUserDetails){
        int rs = replyService.replyUp(replyDto, myUserDetails);

        return rs;
    }

}
