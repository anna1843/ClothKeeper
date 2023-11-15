package org.project.dev.payment.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.project.dev.payment.dto.KakaoPayPrepareDto;
import org.project.dev.payment.dto.PaymentDto;
import org.project.dev.payment.entity.PaymentEntity;
import org.project.dev.payment.repository.PaymentRepository;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    /*
    TODO
    pgToken = kakao pay에서 발행한 결제 준비 토큰  이걸 가지고 결제 승인으로 보내야됨!
    구매자가 상품을 장바구니에 담고! 결제를 누르면 kakao payd에서 발행한 url로 페이지 이동이 되고!
    모바일로 qr코드를 찍고 결제 완료를 누르고! 발행된 pgtoken으로 다시 kakao pay로 결제 요청!을 pg 토큰과 tid를 가지고 전송!
    하면 최종적으로 결제가 완료가 됨!
     */

    /*
    TODO
    catfather49@gmail.com 
    결제 "준비" 승인 아님 카카오에서 요청을 날리고 redirect를 받아서 tic pg_token db에 저장
     */
    @Transactional
    public void paymentApproval(String pgToken, Long paymentId, Long productPrice, String productName, Long memberId) {

        paymentRepository.updatePgToken(paymentId, pgToken);
        PaymentEntity paymentEntity = paymentRepository.findById(paymentId).orElseThrow();
        PaymentDto paymentDto = PaymentDto.toDto(paymentEntity);


        //여기까지 payment 요청을  카카오 api 에 날림 다시 redirect 해서 온 pgToken(카카오에서 보내준 데이터)와
        // pathvarible로 받은 paymentid를 가지고 pgToken을 update 시킴
        //이렇게 할수 밖에 없는 이유 = pgtoken이 url 값으로 날라와서 한번더 db에 저장 시키는 과정이 필요함

        PaymentDto getTidPaymentDto = JSonToObject(paymentDto);
        paymentDto.setTid(getTidPaymentDto.getTid());

        //카카오 결제 승인
        paymentApproveKakao(paymentDto,paymentId, productPrice, productName,memberId);

    }

    //payment 테이블의 readyjson를  dto와 맵핑
    public PaymentDto JSonToObject(PaymentDto dto) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // 필요없는 값 = json 에는 존재하는데 dto에는 없는 값들을 걸러서 파싱

        try {
            PaymentDto paymentDto = objectMapper.readValue(dto.getPaymentReadyJson().toString(), PaymentDto.class);

            return paymentDto;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    // 결제 요청 카카오
    public void paymentApproveKakao(PaymentDto paymentDto, Long paymentId, Long productPrice, String productName, Long memberId) {
        RestTemplate restTemplate = new RestTemplate();
        PaymentEntity paymentEntity = new PaymentEntity();
        ObjectMapper objectMapper = new ObjectMapper();
        String kakaoJsonString = null;

        Optional<PaymentEntity> optionalPaymentEntity = paymentRepository.findById(paymentDto.getPaymentId());

        if (optionalPaymentEntity.isPresent()) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "KakaoAK 6bf92b429a38f0eabe31b6d0642a9a24");
            HttpEntity<String> entity = new HttpEntity<String>(headers);

            URI uri = UriComponentsBuilder
                    //kakao api 주소
                    .fromUriString("https://kapi.kakao.com")
                    .path("/v1/payment/approve")
                    .queryParam("cid", "TC0ONETIME") //가맹점 코드
                    .queryParam("partner_order_id", paymentDto.getPaymentId()) //partner_order_id = paymentId 로 매칭 시켜야함
                    .queryParam("partner_user_id", memberId) //마찬가지로 partner_user_id = member_id로 매칭
                    .queryParam("item_name", productName) //우리 상품 이름
                    .queryParam("tid", paymentDto.getTid()) //
                    .queryParam("total_amount", productPrice) //총 결제 금액
                    .queryParam("tax_free_amount", "100") // 비과세라는데 필수값이라서 10%정도 잡으면 될듯
                    .queryParam("pg_token", paymentDto.getPgToken()) //call back uri
                    .encode()
                    .build()
                    .toUri();

            ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);

            //카카오 paymentJson자체를 컬럼에 insert하려고 to string함
            try {
                kakaoJsonString = objectMapper.writeValueAsString(result.getBody());
                System.out.println(kakaoJsonString);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("kakao payment request : json to string error : " + e);
            }

            //paymentEntity.setPaymentJson(kakaoJsonString);

            paymentRepository.updateIsSucced(paymentDto.getPaymentId(),1);

        } else {
            throw new IllegalArgumentException("해당 아이디에 값이 없습니다.");
        }
    }

    /*
    토스 카카오 네이버 기타등등 pg로 들어오는 거에 따라서 해당 api에 결제 요청을 보냄
    지금 하드 코딩으로 설정 값들 다 보이는데 설정 파일하나로 관리하게...
     */
    @Transactional
    public String pgRequest(String pg, Long productId, Long memberId, Long productPrice, String productName) {
        RestTemplate restTemplate = new RestTemplate();
        PaymentEntity paymentEntity = new PaymentEntity();
        ObjectMapper objectMapper = new ObjectMapper();
        String kakaoJsonString = null;
        Long paymentId;

        if (pg.equals("kakao")) {
            paymentEntity.setPaymentType("KAKAO");
            paymentEntity.setProductPrice(productPrice);
            paymentId = paymentRepository.save(paymentEntity).getPaymentId();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "KakaoAK 6bf92b429a38f0eabe31b6d0642a9a24");
            HttpEntity<String> entity = new HttpEntity<String>(headers);

            URI uri = UriComponentsBuilder
                    //kakao api 주소
                    .fromUriString("https://kapi.kakao.com")
                    .path("/v1/payment/ready")
                    .queryParam("cid", "TC0ONETIME")
                    .queryParam("partner_order_id", paymentId) //partner_order_id = paymentId 로 매칭 시켜야함
                    .queryParam("partner_user_id", memberId) //마찬가지로 partner_user_id = member_id로 매칭
                    .queryParam("item_name", productName) //우리 상품 이름
                    .queryParam("quantity", "1") //구매자가 설정한 상품 갯수
                    .queryParam("total_amount", productPrice) //총 결제 금액
                    .queryParam("tax_free_amount", "100") // 비과세라는데 필수값이라서 10%정도 잡으면 될듯
                    .queryParam("approval_url", "http://localhost:8095/payment/approval/"
                            +paymentId+"/"+productPrice+"/"+productName+"/"+memberId) //call back uri
                    .queryParam("cancel_url", "http://localhost:8095/payment/cancel") //주문 취소
                    .queryParam("fail_url", "http://localhost:8095/payment/fail") //실패
                    .encode()
                    .build()
                    .toUri();
            ResponseEntity<KakaoPayPrepareDto> result = restTemplate.exchange(uri, HttpMethod.POST, entity, KakaoPayPrepareDto.class);
            //카카오 paymentJson자체를 컬럼에 insert하려고 to string함
            try {
                kakaoJsonString = objectMapper.writeValueAsString(result.getBody());
            } catch (JsonProcessingException e) {
                throw new RuntimeException("kakao payment request : json to string error : " + e);
            }
            paymentEntity.setPaymentReadyJson(kakaoJsonString);
            paymentRepository.save(paymentEntity);
            return result.getBody().getNext_redirect_pc_url();

        } else {
            throw new RuntimeException("제휴되지 않은 결제 업체 입니다.!!!!");
        }

    }

}
