package org.project.dev.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.dev.config.member.MyUserDetails;
import org.project.dev.config.member.UserDetailsServiceImpl;
import org.project.dev.member.dto.ImageResponseDto;
import org.project.dev.member.dto.ImageUploadDto;
import org.project.dev.member.dto.MemberDto;
import org.project.dev.member.entity.ImageEntity;
import org.project.dev.member.entity.MemberEntity;
import org.project.dev.member.repository.ImageRepository;
import org.project.dev.member.repository.MemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final MemberRepository memberRepository;
    private final UserDetailsServiceImpl userDetailsService;

    // 이미지 URL 기본값 설정(이미지 없을 경우 기본 이미지로 설정)
    private final String defaultImageUrl = "/profileImages/default.png";

    @Value("${file.profileImgUploadDir}")
    private String uploadFolder;


    // 이미지 등록
    @Override
    public void upload(ImageUploadDto imageUploadDto, String memberEmail) {
        MemberEntity member = memberRepository.findByMemberEmail(memberEmail).orElseThrow(() ->
                new UsernameNotFoundException("이메일이 존재하지 않습니다"));

        MultipartFile file = imageUploadDto.getFile();
        String imageFileName;

        if (file.isEmpty()) {
            // 파일이 업로드되지 않은 경우 기본 이미지 이름을 사용
            imageFileName = "default.png";
        } else {
            UUID uuid = UUID.randomUUID();
            imageFileName = uuid + "_" + file.getOriginalFilename();

            File destinationFile = new File(uploadFolder + imageFileName);

            try {
                file.transferTo(destinationFile);
            } catch (IOException e) {
                log.info("이미지 업로드 중 오류 발생", e);
                throw new RuntimeException("이미지 저장 실패", e);
            }
        }

        ImageEntity image = imageRepository.findByMember(member);
        if (image != null) {
            // 이미지가 이미 존재하면 URL 업데이트
            image.updateUrl("/profileImages/" + imageFileName);
        } else {
            // 이미지가 없으면 객체 생성 후 저장
            image = ImageEntity.builder()
                    .member(member)
                    .imageUrl("/profileImages/" + imageFileName)
                    .build();
            log.info("이미지 업로드 및 저장 성공");
        }
        imageRepository.save(image);

        // 이미지 URL을 로그로 출력
        log.info("이미지 URL: " + image.getImageUrl());
    }


    // 이미지 삭제
    @Override
    public void deleteImage(String memberEmail) {
        MemberEntity member = memberRepository.findByMemberEmail(memberEmail).orElseThrow(() ->
                new UsernameNotFoundException("이메일이 존재하지 않습니다"));

        ImageEntity image = imageRepository.findByMember(member);
        if (image != null) {
            // 이미지가 존재하면 삭제
            try {
                if (!image.getImageUrl().equals("/profileImages/default.png")) {
                    // 기본 이미지가 아닌 경우에만 삭제
                    File imageFile = new File(uploadFolder + image.getImageUrl());
                    if (imageFile.exists()) {
                        imageFile.delete();
                    }
                }
            } catch (Exception e) {
                log.error("이미지 삭제 중 오류 발생", e);
                throw new RuntimeException("이미지 삭제 실패", e);
            }
            imageRepository.delete(image);
            log.info("이미지 삭제 성공");
        }
    }
    
    // 회원 프로필 이미지 조회
    public ImageResponseDto findImage(String memberEmail){
        MemberEntity member =memberRepository.findByMemberEmail(memberEmail).orElseThrow(()->
                new UsernameNotFoundException("이메일이 존재하지않습니다"));
        ImageEntity image = imageRepository.findByMember(member);

        String defaultImageUrl = "/profileImages/default.png";

        if (image == null) {
            return ImageResponseDto.builder()
                    .imageUrl(defaultImageUrl)
                    .build();
        }else {
            return ImageResponseDto.builder()
                    .imageUrl(image.getImageUrl())
                    .build();
        }
    }

}
