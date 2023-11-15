package org.project.dev.cart.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.dev.cart.entity.CartEntity;
import org.project.dev.cart.entity.CartItemEntity;
import org.project.dev.cart.service.SemiCartService;
import org.project.dev.config.semiMember.SemiMyUserDetails;
import org.project.dev.member.entity.SemiMemberEntity;
import org.project.dev.member.service.SemiMemberService;
import org.project.dev.product.dto.ProductImgDTO;
import org.project.dev.product.entity.ProductEntity;
import org.project.dev.product.service.ProductService;
import org.project.dev.product.service.ProductUtilService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/cart")
public class SemiCartController {

    private final SemiCartService semiCartService;
    private final SemiMemberService semiMemberService;
    private final ProductService productService;
    private final ProductUtilService productUtilService;

    // 장바구니에 물건 담기
    @PostMapping("/semiMember/{semiMemberId}/{id}")
    public String postAddCartItem(@PathVariable("semiMemberId") Long semiMemberId,
                                  @PathVariable("id") Long productId, int amount){

        SemiMemberEntity semiMember=semiMemberService.findSemiMember(semiMemberId);
        ProductEntity product=productService.productView(productId);

        semiCartService.addCart(semiMember, product, amount);

        return "redirect:/product/"+productId;
//        return "redirect:/product/{productId}";
    }


    // 장바구니 페이지
    @GetMapping("/semiMember/{semiMemberId}")
    public String getCartView(@PathVariable("semiMemberId") Long semiMemberId, Model model,
                              @AuthenticationPrincipal SemiMyUserDetails semiMyUserDetails){
        // 로그인 되어있는 유저의 id와 장바구니에 접속하는 id가 같아야함
        if (semiMyUserDetails.getSemiMemberEntity().getSemiMemberId()==semiMemberId) {

            SemiMemberEntity semiMember=semiMemberService.findSemiMember(semiMemberId);
            // 로그인 되어있는 유저에 해당하는 장바구니 가져오기
            CartEntity semiMemberCart=semiMember.getCart();

            // 장바구니에 들어있는 상품 모두 가져오기
            List<CartItemEntity> cartItemEntityList=semiCartService.allSemiMemberCartView(semiMemberCart);

            // 장바구니에 들어있는 상품들의 총 가격
            int totalPrice=0;
            for(CartItemEntity cartItem: cartItemEntityList){
                totalPrice += cartItem.getCartItemCount() * cartItem.getProduct().getProductPrice();
            }

            // 상품 이미지를 가져와서 모델에 추가
            List<ProductImgDTO> productImages = new ArrayList<>();
            for (CartItemEntity cartItem : cartItemEntityList) {
                Long productId = cartItem.getProduct().getId();
                List<ProductImgDTO> productImgs = productUtilService.getProductImagesByProductId(productId);
                productImages.addAll(productImgs);
            }

            model.addAttribute("totalPrice", totalPrice);
            model.addAttribute("totalCount", semiMemberCart.getCartCount());
            model.addAttribute("cartItems", cartItemEntityList);
            model.addAttribute("semiMember", semiMemberService.findSemiMember(semiMemberId));
            model.addAttribute("productImages", productImages);

            return "/semiMember/cart";
        }else {
            // 로그인 id와 장바구니 접속 id가 같지 않은 경우
            return "redirect:/";
        }
    }

    // 장바구니에서 상품 삭제
    // 삭제하고 남은 상품의 총 갯수
    @GetMapping("/semiMember/{semiMemberId}/{cartItemId}/delete")
    public String getDeleteCartItem(@PathVariable("semiMemberId") Long semiMemberId,
                                    @PathVariable("cartItemId") Long cartItemId, Model model,
                                    @AuthenticationPrincipal SemiMyUserDetails semiMyUserDetails){

        // 로그인 멤버 id와 장바구니 멤버 id가 같아야함
        if (semiMyUserDetails.getSemiMemberEntity().getSemiMemberId()==semiMemberId) {
            // cartItemId로 장바구니 상품 찾기
            CartItemEntity cartItem=semiCartService.findCartItemById(cartItemId);

            // 장바구니 물건 삭제
            semiCartService.cartItemDelete(cartItemId);

            // 해당 멤버의 카트 찾기
            CartEntity semiMemberCart=semiCartService.findSemiMemberCart(semiMemberId);

            // 해당 멤버의 장바구니 상품들
            List<CartItemEntity> cartItemEntityList=semiCartService.allSemiMemberCartView(semiMemberCart);

            // 총 가격 += 수량*가격
            int totalPrice=0;
            for(CartItemEntity cartItem1: cartItemEntityList){
                totalPrice += cartItem1.getCartItemCount() * cartItem1.getProduct().getProductPrice();
            }

            semiMemberCart.setCartCount(semiMemberCart.getCartCount() - cartItem.getCartItemCount());

            model.addAttribute("totalPrice", totalPrice);
            model.addAttribute("totalCount", semiMemberCart.getCartCount());
            model.addAttribute("cartItems", cartItemEntityList);
            model.addAttribute("semiMember", semiMemberService.findSemiMember(semiMemberId));

            return "redirect:/cart/semiMember/" + semiMyUserDetails.getSemiMemberEntity().getSemiMemberId();

        }else {
            // 로그인id와 장바구니 삭제하려는 멤버id가 같지 않은 경우
            return "redirect:/";
        }
    }

}
