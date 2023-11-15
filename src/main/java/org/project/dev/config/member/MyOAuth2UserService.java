package org.project.dev.config.member;
import lombok.extern.slf4j.Slf4j;
import org.project.dev.cart.entity.CartEntity;
import org.project.dev.cart.repository.CartRepository;
import org.project.dev.constrant.Role;
import org.project.dev.member.dto.ImageUploadDto;
import org.project.dev.member.entity.ImageEntity;
import org.project.dev.member.entity.MemberEntity;
import org.project.dev.member.repository.ImageRepository;
import org.project.dev.member.repository.MemberRepository;
import org.project.dev.member.service.ImageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Slf4j
@Service
public class MyOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ImageServiceImpl imageService;

    // 이미지 URL 기본값 설정(이미지 없을 경우 기본 이미지로 설정)
    private String defaultImageUrl = "/profileImages/default.png";

    @Value("${file.productImgUploadDir}")
    private String uploadFolder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        String clientId = clientRegistration.getClientId();
        String registrationId = clientRegistration.getRegistrationId();
        String registrationName = clientRegistration.getClientName();

        // 테스트하고 잘 나오면 지우기
        System.out.println("==================================");
        System.out.println("clientId : " + clientId);
        System.out.println("registrationId : " + registrationId);
        System.out.println("registrationName : " + registrationName);
        System.out.println("==================================");

        Map<String, Object> attributes = oAuth2User.getAttributes();

        for (String key : attributes.keySet()) {
            System.out.println(key + ": " + attributes.get(key));
        }

        String memberEmail = "";
        String memberPassword = "dlfwhghkdlxld";
        String memberName = "";
        String memberNickName = "";
        String memberPhone="";
        String memberBirth="";
        String memberPostCode="";


        if (registrationId.equals("google")) {
            System.out.println("google 로그인");

            memberEmail = (String) attributes.get("email");
            memberName = (String) attributes.get("name");

        } else if (registrationId.equals("naver")) {
            System.out.println("naver 로그인");

            Map<String, Object> response = (Map<String, Object>) attributes.get("response");

            memberEmail = (String) response.get("email");
            memberName= (String) response.get("name");
            memberNickName = (String) response.get("nickname");
//            memberPhone=(String) response.get("mobile");
            // 휴대전화번호 데이터 형식 변환
            memberPhone = transformPhoneNumber((String) response.get("mobile"));
            memberBirth=(String) response.get("birthday");

            System.out.println((String) response.get("id"));
            System.out.println((String) response.get("email"));
            System.out.println((String) response.get("name"));

        } else if (registrationId.equals("kakao")) {
            System.out.println("kakao 로그인");

            Map<String, Object> response = (Map<String, Object>) attributes.get("kakao_account");
            System.out.println("response : " + response);
            System.out.println("response - email : " + response.get("email"));
            System.out.println("response - birth : " + response.get("birthday"));

            Map<String, Object> profile = (Map<String, Object>) response.get("profile");
            System.out.println("response - nickname : " + profile.get("nickname"));

            memberEmail = (String) response.get("email");
            memberNickName = (String) profile.get("nickname");
            memberBirth=(String) response.get("birthday");
        }

        // 휴대전화번호가 비어있는 경우 임의의 15자리 숫자 생성
        if (memberPhone.isEmpty()) {
            memberPhone = generateRandomPhoneNumber();
        }

        Optional<MemberEntity> optionalMemberEntity = memberRepository.findByMemberEmail(memberEmail);
        if (optionalMemberEntity.isPresent()) {

//            OAuthUser → DB 비교, 있으면
            return new MyUserDetails(optionalMemberEntity.get());
        }

        MemberEntity memberEntity = memberRepository.save(MemberEntity.builder()
                .memberEmail(memberEmail)
                .memberPassword(passwordEncoder.encode(memberPassword))
                .memberName(memberName)
                .memberNickName(memberNickName)
                .memberPhone(memberPhone)
                .memberBirth(memberBirth)
                .memberPostCode(memberPostCode)
                .role(Role.ADMIN)
                .build());
        
        // 회원가입 후 장바구니 생성
        createCartForMember(memberEntity);

        // OAuth 로그인 후 이미지를 업로드합니다.
        ImageUploadDto imageUploadDto = new ImageUploadDto();
        upload(imageUploadDto, memberEmail);

        return new MyUserDetails(memberEntity, attributes);
    }

    // 휴대전화번호가 비어있는 경우 임의의 15자리 숫자 생성 메서드
    private String generateRandomPhoneNumber() {
        StringBuilder sb = new StringBuilder();
        Random rand = new Random();

        for (int i = 0; i < 15; i++) {
            sb.append(rand.nextInt(10)); // 0부터 9까지의 난수 생성
        }
        return sb.toString();
    }

    // 기본 이미지 저장, 등록 메서드
    public void upload(ImageUploadDto imageUploadDto, String memberEmail) {
        MemberEntity member = memberRepository.findByMemberEmail(memberEmail).orElseThrow(() ->
                new UsernameNotFoundException("이메일이 존재하지 않습니다"));

        MultipartFile file = imageUploadDto.getFile();
        String imageFileName;

        if (file == null || file.isEmpty()) {
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
    }
    
    // 장바구니 생성 메서드
    private void createCartForMember(MemberEntity memberEntity) {
        CartEntity cart = CartEntity.createCart(memberEntity);
        cartRepository.save(cart);
    }

    // 휴대전화번호 db에 저장될때 해당 형식으로 저장되도록 하는 메서드
    private String transformPhoneNumber(String memberPhone) {
        // 입력된 휴대전화번호에서 "-" 문자를 제거
        String cleanedPhoneNumber = memberPhone.replaceAll("-", "");

        return cleanedPhoneNumber;
    }
}
