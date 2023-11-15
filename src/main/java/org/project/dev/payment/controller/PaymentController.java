package org.project.dev.payment.controller;

import lombok.RequiredArgsConstructor;
import org.project.dev.payment.dto.PaymentDto;
import org.project.dev.payment.service.PaymentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/request")
    public Map<String, Object> request(
            @RequestBody PaymentDto paymentDto,
            @RequestParam("memberid") String memberId) {

        return null;
    }
    @GetMapping("/paymentTestPg")
    public String testPg() {

        return "payment/paymentIndex";
    }


    /*
    TODO
    catfather49@gmail.com
     */
    @GetMapping("approval/{paymentId}/{productPrice}/{productName}/{memberId}")
    public String approval(
            @PathVariable(name = "paymentId") Long paymentId,
            @PathVariable(name = "productPrice") Long productPrice,
            @PathVariable(name = "productName") String productName,
            @PathVariable(name = "memberId") Long memberId,
            @RequestParam("pg_token") String pgToken) {
        Map<String, Object> paymentMap = new HashMap<String, Object>();
        paymentService.paymentApproval(pgToken, paymentId,productPrice,productName,memberId);
        return "payment/succed";
    }


    /*
    TODO
    1.catfather49@gmail.com
    2.productId, cartId, totalPrice, itemPrice, itemName,
    3. return 으로 result pc 앱 결제 url 만 설정
     */
    @GetMapping("/{pg}/pg")
    @ResponseBody
    public Map<String, Object> pgRequest(
            @PathVariable("pg") String pg,
            @RequestParam("productId") Long productId,
            @RequestParam("memberId") Long memberId, // myuserdetail로 가져오기
            @RequestParam("productPrice") Long productPrice,
            @RequestParam("productName") String productName
    ) {
        Map<String, Object> map = new HashMap<String, Object>();
        String approvalUrl = paymentService.pgRequest(pg, productId, memberId, productPrice, productName);
        map.put("approvalUrl", approvalUrl);
        return map;

    }

    @PostMapping("/fail")
    public Map<String, Object> fail(
            @RequestBody PaymentDto paymentDto,
            @RequestParam("memberid") String memberId) {

        return null;
    }
}
