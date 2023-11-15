package org.project.dev.cart.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.dev.cart.entity.CartEntity;
import org.project.dev.cart.entity.CartItemEntity;
import org.project.dev.cart.service.CartService;
import org.project.dev.config.member.MyUserDetails;
import org.project.dev.member.entity.MemberEntity;
import org.project.dev.member.service.ImageServiceImpl;
import org.project.dev.member.service.MemberService;
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
public class CartController {

    private final CartService cartService;
    private final MemberService memberService;
    private final ProductService productService;
    private final ProductUtilService productUtilService;
    private final ImageServiceImpl imageService;

    // 장바구니에 물건 담기
    @PostMapping("/member/{memberId}/{id}")
    public String postAddCartItem(@PathVariable("memberId") Long memberId,
                                  @PathVariable("id") Long productId, int amount){

        MemberEntity member=memberService.findMember(memberId);
        ProductEntity product=productService.productView(productId);

        cartService.addCart(member, product, amount);

        return "redirect:/product/" + productId;
    }

    // 장바구니 페이지
    @GetMapping("/member/{memberId}")
    public String getCartView(@PathVariable("memberId") Long memberId, Model model,
                              @AuthenticationPrincipal MyUserDetails myUserDetails){
        // 로그인 되어있는 유저의 id와 장바구니에 접속하는 id가 같아야함
        if (myUserDetails.getMemberEntity().getMemberId()==memberId) {

            // 프로필 이미지 불러옴
            MemberEntity member=memberService.findMember(memberId);
            String memberImageUrl = imageService.findImage(member.getMemberEmail()).getImageUrl();

            // 로그인 되어있는 유저에 해당하는 장바구니 가져오기
            CartEntity memberCart=member.getCart();

            // 장바구니에 들어있는 상품 모두 가져오기
            List<CartItemEntity> cartItemEntityList=cartService.allMemberCartView(memberCart);

            // 장바구니에 들어있는 상품들의 총 가격
            int totalPrice=0;
            for(CartItemEntity cartItem: cartItemEntityList){
                totalPrice += cartItem.getCartItemCount() * cartItem.getProduct().getProductPrice();
            }

            // 상품 이미지를 가져와서 모델에 추가
            List<ProductImgDTO> productImages = new ArrayList<>();
            List<ProductEntity> productEntities = new ArrayList<ProductEntity>();
            for (CartItemEntity cartItem : cartItemEntityList) {
                Long productId = cartItem.getProduct().getId();
                productEntities.add(productService.productView(productId));
                List<ProductImgDTO> productImgs = productUtilService.getProductImagesByProductId(productId);
                productImages.addAll(productImgs);
            }

            model.addAttribute("totalPrice", totalPrice);
            model.addAttribute("totalCount", memberCart.getCartCount());
            model.addAttribute("cartItems", cartItemEntityList);
            model.addAttribute("member", memberService.findMember(memberId));
            model.addAttribute("productImages", productImages);
            model.addAttribute("product", productEntities.get(0));
            model.addAttribute("memberImageUrl", memberImageUrl); // 프로필 이미지 불러옴

            return "member/cart";

        }else {
            // 로그인 id와 장바구니 접속 id가 같지 않은 경우
            return "redirect:/";
        }
    }

    // 장바구니에서 상품 삭제
    // 삭제하고 남은 상품의 총 갯수
    @GetMapping("/member/{memberId}/{cartItemId}/delete")
    public String getDeleteCartItem(@PathVariable("memberId") Long memberId,
                                    @PathVariable("cartItemId") Long cartItemId, Model model,
                                    @AuthenticationPrincipal MyUserDetails myUserDetails){

        // 로그인 멤버 id와 장바구니 멤버 id가 같아야함
        if (myUserDetails.getMemberEntity().getMemberId() == memberId) {
            // cartItemId로 장바구니 상품 찾기
            CartItemEntity cartItem=cartService.findCartItemById(cartItemId);

            // 장바구니 물건 삭제
            cartService.cartItemDelete(cartItemId);

            // 해당 멤버의 카트 찾기
            CartEntity memberCart=cartService.findMemberCart(memberId);

            // 해당 멤버의 장바구니 상품들
            List<CartItemEntity> cartItemEntityList=cartService.allMemberCartView(memberCart);

            // 총 가격 += 수량*가격
            int totalPrice=0;
            for(CartItemEntity cartItem1: cartItemEntityList){
                totalPrice += cartItem1.getCartItemCount() * cartItem1.getProduct().getProductPrice();
            }

            memberCart.setCartCount(memberCart.getCartCount() - cartItem.getCartItemCount());

            model.addAttribute("totalPrice", totalPrice);
            model.addAttribute("totalCount", memberCart.getCartCount());
            model.addAttribute("cartItems", cartItemEntityList);
            model.addAttribute("member", memberService.findMember(memberId));

            return "redirect:/cart/member/" + myUserDetails.getMemberEntity().getMemberId();

        }else {
            // 로그인id와 장바구니 삭제하려는 멤버id가 같지 않은 경우
            return "redirect:/";
        }
    }
}
