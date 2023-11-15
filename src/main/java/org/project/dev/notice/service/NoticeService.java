package org.project.dev.notice.service;

import lombok.RequiredArgsConstructor;
import org.project.dev.config.member.MyUserDetails;
import org.project.dev.member.entity.MemberEntity;
import org.project.dev.notice.dto.NoticeDto;
import org.project.dev.notice.entity.NoticeEntity;
import org.project.dev.notice.repository.NoticeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.IllformedLocaleException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    /*
   Todo
    1. rladpwls1843@gamil.com
    2. 공지사항 쓰기 서비스
    3.
    4.
    */
    @Transactional
    public int noticeInsert(NoticeDto noticeDto, MyUserDetails myUserDetails) {

        myUserDetails.getMemberEntity();

        NoticeEntity noticeEntity = NoticeEntity.toNoticeEntityInsert(noticeDto);

        Long noticeId = noticeRepository.save(noticeEntity).getNotId(); // entity값을 받아서 repository에 저장

        Optional<NoticeEntity> optionalNoticeEntity
                = Optional.ofNullable(noticeRepository.findById(noticeId).orElseThrow(() -> {
            throw  new IllegalArgumentException("공지사항을 찾을 수 없습니다.");
        }));

        if(!optionalNoticeEntity.isPresent()){
            return 0;
        }
        return 1;
    }

    /*
       Todo
        1. rladpwls1843@gamil.com
        2. 공지사항 목록 서비스
        3.
        4.
    */

    //    @Transactional
//    public Page<NoticeDto> noticeList(Pageable pageable, String noticeSelect, String noticeSearch,
//                                      MyUserDetails myUserDetails) {
//
//        myUserDetails.getMemberEntity();
//        Page<NoticeEntity> noticeEntities = null; // 기본 null 값으로 설정
//
//        if (noticeSelect.equals("noticeTitle")) {
//            noticeEntities = noticeRepository.findByNoticeTitleContaining(pageable, noticeSearch); // title에 해당하는 검색 내용
//        } else if (noticeSelect.equals("noticeContent")) {
//            noticeEntities = noticeRepository.findByNoticeContentContaining(pageable, noticeSearch); // content에 해당하는 검색 내용
//        } else {
//            noticeEntities = noticeRepository.findAll(pageable); // 위 2가지가 아닌 경우 모든 리스트
//        }
//
//        noticeEntities.getNumber();
//        noticeEntities.getTotalElements();
//        noticeEntities.getTotalPages();
//        noticeEntities.getSize();
//
//        Page<NoticeDto> noticeDtoPage = noticeEntities.map(NoticeDto::tonoticeDto);
//
//        return noticeDtoPage;
//
//    }
    @Transactional
    public Page<NoticeDto> noticeList(Pageable pageable, String noticeSelect, String noticeSearch) {

        Page<NoticeEntity> noticeEntities = null; // 기본 null 값으로 설정

        if (noticeSelect.equals("noticeTitle")) {
            noticeEntities = noticeRepository.findByNoticeTitleContaining(pageable, noticeSearch); // title에 해당하는 검색 내용
        } else if (noticeSelect.equals("noticeContent")) {
            noticeEntities = noticeRepository.findByNoticeContentContaining(pageable, noticeSearch); // content에 해당하는 검색 내용
        } else {
            noticeEntities = noticeRepository.findAll(pageable); // 위 2가지가 아닌 경우 모든 리스트
        }

        noticeEntities.getNumber();
        noticeEntities.getTotalElements();
        noticeEntities.getTotalPages();
        noticeEntities.getSize();

        Page<NoticeDto> noticeDtoPage = noticeEntities.map(NoticeDto::tonoticeDto);

        return noticeDtoPage;

    }
    /*
       Todo
        1. rladpwls1843@gamil.com
        2. 공지사항 목록로 이동
        3.
        4. type에 해당하는 목록으로 이동
        */
//    @Transactional
//    public Page<NoticeDto> noticeList(String type, Pageable pageable, MyUserDetails myUserDetails) {
//
//
//        myUserDetails.getMemberEntity();
//
//        Page<NoticeEntity> noticeEntities = noticeRepository.findByNotType(type, pageable); // not_type에 해당하는 값만 출력
//
//        noticeEntities.getNumber();
//        noticeEntities.getTotalElements();
//        noticeEntities.getTotalPages();
//        noticeEntities.getSize();
//
//        Page<NoticeDto> noticeDtoPage = noticeEntities.map(NoticeDto::tonoticeDto);
//
//        return noticeDtoPage;
//    }
    @Transactional
    public Page<NoticeDto> noticeList(String type, Pageable pageable) {


        Page<NoticeEntity> noticeEntities = noticeRepository.findByNotType(type, pageable); // not_type에 해당하는 값만 출력

        noticeEntities.getNumber();
        noticeEntities.getTotalElements();
        noticeEntities.getTotalPages();
        noticeEntities.getSize();

        Page<NoticeDto> noticeDtoPage = noticeEntities.map(NoticeDto::tonoticeDto);

        return noticeDtoPage;
    }

    /*
           Todo
            1. rladpwls1843@gamil.com
            2. 공지사항 상세페이지로 이동
            3.
            4.
     */
    @Transactional
    public NoticeDto noticeDetail(Long id) {

        noticeHit(id);

        NoticeEntity noticeEntity = noticeRepository.findById(id).orElseThrow(IllformedLocaleException::new);

        return NoticeDto.builder()
                .notId(noticeEntity.getNotId())
                .noticeTitle(noticeEntity.getNoticeTitle())
                .noticeContent(noticeEntity.getNoticeContent())
                .notType(noticeEntity.getNotType())
                .CreateTime(noticeEntity.getCreateTime())
                .UpdateTime(noticeEntity.getUpdateTime())
                .notWriter(noticeEntity.getNotWriter())
                .notHit(noticeEntity.getNotHit())
                .build();
    }
    /*
       Todo
        1. rladpwls1843@gamil.com
        2. 상세페이지 선택할 떄마다 조회수 올라감
        3.
        4.
        */
    @Transactional
    public void noticeHit(Long id){
        noticeRepository.NoticeHit(id);
    }

    /*
       Todo
        1. rladpwls1843@gamil.com
        2. 공지사항 수정 서비스
        3.
        4.
        */
    public NoticeDto noticeUpdate(Long id) {

        Optional<NoticeEntity> optionalNoticeEntity
                = Optional.ofNullable(noticeRepository.findById(id).orElseThrow(() -> {
            return new IllegalArgumentException("공지사항을 찾을 수 없습니다.");
        }));
        if(optionalNoticeEntity.isPresent()){
            NoticeDto noticeDto = NoticeDto.tonoticeDto(optionalNoticeEntity.get());

            return noticeDto;
        }
        return null;
    }

    @Transactional
    public NoticeDto noticeUpdateOk(NoticeDto noticeDto, Long id) {
        // errer throw 처리
        NoticeEntity noticeEntity = noticeRepository.findById(id).orElseThrow(()->{
            throw new IllegalArgumentException("수정할 공지사항이 존재하지 않습니다.");
        });
        Long noticeId = noticeRepository.save(NoticeEntity.toNoticeEntityUpdate(noticeDto)).getNotId(); // 수정을 위한 jparepository

        NoticeEntity noticeEntity1 = noticeRepository.findById(noticeId).orElseThrow(()->{
            throw new IllegalArgumentException("수정할 공지사항이 존재하지 않습니다.");
        });
        return NoticeDto.tonoticeDto(noticeEntity1);
    }

    /*
           Todo
            1. rladpwls1843@gamil.com
            2. 공지사항 삭제 서비스
            3.
            4.
            */
    @Transactional
    public int noticeDelete(Long id) {

        Optional<NoticeEntity> optionalNoticeEntity
                = Optional.ofNullable(noticeRepository.findById(id).orElseThrow(() -> {
            return new IllegalArgumentException("삭제할 공지사항이 없습니다.");
        }));
        noticeRepository.delete(optionalNoticeEntity.get());

        Optional<NoticeEntity> optionalNoticeEntity1 = noticeRepository.findById(id);
        if (!optionalNoticeEntity.isPresent()){
            return 1;
        }
        return 0;
    }

}