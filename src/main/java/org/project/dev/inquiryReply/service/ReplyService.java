package org.project.dev.inquiryReply.service;

import lombok.RequiredArgsConstructor;
import org.project.dev.config.member.MyUserDetails;
import org.project.dev.inquiryReply.dto.ReplyDto;
import org.project.dev.inquiryReply.entity.ReplyEntity;
import org.project.dev.inquiryReply.repository.ReplyRepository;
import org.project.dev.notice.entity.InquiryEntity;
import org.project.dev.notice.repository.InquiryRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;

    private final InquiryRepository inquiryRepository;

    public ReplyDto replyWrite(ReplyDto replyDto, MyUserDetails myUserDetails) {

        InquiryEntity inquiryEntity
                = inquiryRepository.findById(replyDto.getInqId()).orElseThrow(() -> new IllegalArgumentException("X"));
//        String replyName = "m1";
        ReplyEntity replyEntity = ReplyEntity.builder()
                .reply(replyDto.getReply())
//                .replyWriter(replyDto.getReplyWriter())
                .replyWriter(myUserDetails.getMemberEntity().getMemberNickName())
                .inquiryEntity(inquiryEntity)
                .build();
        Long replyId = replyRepository.save(replyEntity).getId();

        ReplyEntity replyEntity1 = replyRepository.findById(replyId).orElseThrow(() -> new IllegalArgumentException("X"));

        return ReplyDto.builder()
                .id(replyEntity1.getId())
                .reply(replyEntity1.getReply())
                .updateTime(replyEntity1.getUpdateTime())
                .createTime(replyEntity1.getCreateTime())
                .inqId(replyEntity1.getInquiryEntity().getInqId())
                .build();
    }


    public List<ReplyDto> replyList(ReplyDto replyDto) {

        InquiryEntity inquiryEntity = inquiryRepository.findById(replyDto.getInqId()).orElseThrow(() -> {
            throw new IllegalArgumentException("X");
        });
        List<ReplyDto> replyDtos = new ArrayList<>();
        List<ReplyEntity> replyEntities = replyRepository.findByInquiryId(inquiryEntity.getInqId());

        for (ReplyEntity replyEntity : replyEntities) {

            replyDtos.add(ReplyDto.builder()
                    .id(replyEntity.getId())
                    .replyWriter(replyEntity.getReplyWriter())
                    .reply(replyEntity.getReply())
                    .inqId(replyEntity.getInquiryEntity().getInqId())
                    .createTime(replyEntity.getCreateTime())
                    .updateTime(replyEntity.getUpdateTime())
                    .build());
        }
        return replyDtos;
    }

    public int replyDelete(Long id) {

        Optional<ReplyEntity> replyEntity
                = Optional.ofNullable(replyRepository.findById(id).orElseThrow(() -> {
            throw new IllegalArgumentException("X");
        }));
        if (replyEntity.isPresent()) {
            replyRepository.delete(replyEntity.get());
            return 1;
        }
        return 0;
    }


    public int replyUp(ReplyDto replyDto, MyUserDetails myUserDetails) {

        InquiryEntity inquiryEntity
                = inquiryRepository.findById(replyDto.getInqId()).orElseThrow(IllegalArgumentException::new);
        replyDto.setReplyWriter("m1");
        ReplyEntity replyEntity = ReplyEntity.builder()
                .id(replyDto.getId())
                .reply(replyDto.getReply())
                .replyWriter(replyDto.getReplyWriter())
                .inquiryEntity(inquiryEntity)
                .build();
        Long saveId = replyRepository.save(replyEntity).getId();

        Optional<ReplyEntity> optionalReplyEntity
                = Optional.ofNullable(replyRepository.findById(saveId).orElseThrow(()->{
                    throw new IllegalArgumentException("X");
        }));
        if (optionalReplyEntity.isPresent()){
            return 1;
        }
        return 0;
    }
}
