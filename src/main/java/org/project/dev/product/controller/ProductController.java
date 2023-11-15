package org.project.dev.product.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.dev.cart.entity.CartEntity;
import org.project.dev.cart.entity.CartItemEntity;
import org.project.dev.cart.service.CartService;
import org.project.dev.cart.service.SemiCartService;
import org.project.dev.config.member.MyUserDetails;
import org.project.dev.config.semiMember.SemiMyUserDetails;
import org.project.dev.member.dto.MemberDto;
import org.project.dev.member.entity.MemberEntity;
import org.project.dev.member.entity.SemiMemberEntity;
import org.project.dev.member.service.ImageServiceImpl;
import org.project.dev.member.service.MemberService;
import org.project.dev.member.service.SemiMemberService;
import org.project.dev.product.dto.ProductBrandDTO;
import org.project.dev.product.dto.ProductCategoryDTO;
import org.project.dev.product.dto.ProductImgDTO;
import org.project.dev.product.entity.ProductBrandEntity;
import org.project.dev.product.entity.ProductCategoryEntity;
import org.project.dev.product.entity.ProductEntity;
import org.project.dev.review.repository.ReviewRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.project.dev.product.dto.ProductDTO;
import org.project.dev.product.service.ProductService;
import org.project.dev.product.service.ProductUtilService;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.*;
import java.io.IOException;
import java.util.*;


@Slf4j // 송원철, log
@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    /*
    Todo
     1. code.aiaru@gmail.com
     2. 상품 관련 CRUD 입니다.
        각 html(detail, list, update, write) 기본 로직들은 만들어놨으니 확인 부탁드려요.
        list.html는 페이징 처리 때문에 list.css를 만들어 뒀습니다. 필수 로직이 있으니 작업 전에 물어봐주세요.
     3. x
     4. 만약 저번에 얘기가 나왔던 것처럼 판매자 접속 URL(판매자 전용 페이지)을 따로 만든다면,
        1) Write, Update 쪽은 판매자 접속 URL 쪽으로 넘기고,
        2) Detail, List는 각 2개 씩 만들어서 하나는 판매자 전용, 하나는 회원/비회원 상품 조회용으로 만들어도 될 것 같습니다.
        이 부분은 같이 상의를...
     */


    private final ProductService productService;
    private final ProductUtilService productUtilService;
    private final ReviewRepository reviewRepository;
    private final MemberService memberService; // 송원철 / 장바구니 관련
    private final CartService cartService; // 송원철 / 장바구니 관련
    private final SemiMemberService semiMemberService; // 송원철 / 장바구니 관련
    private final SemiCartService semiCartService; // 송원철 / 장바구니 관련
    private final ImageServiceImpl imageService; // 송원철 / header 관련
    


    // 송원철 / write에 로그인한 memberId 담기
    @GetMapping("/write")
    public String getProductWrite(@AuthenticationPrincipal MyUserDetails myUserDetails, Model model) {

        MemberEntity member=myUserDetails.getMemberEntity();
        String memberImageUrl = imageService.findImage(member.getMemberEmail()).getImageUrl();

        if (member != null) {
            model.addAttribute("member", member);
            model.addAttribute("memberImageUrl", memberImageUrl);
            log.info("MemberId: {}", member.getMemberId());
        }else {
            log.info("member is null");
        }
        return "product/write";
    }

    // 송원철 / write 시 로그인한 memberId, memberNickName 저장
    @PostMapping("/write")
    public ResponseEntity<Map<String,Object>> postProductWrite(@ModelAttribute ProductDTO productDTO,
                                                               @ModelAttribute ProductCategoryDTO productCategoryDTO,
                                                               @ModelAttribute ProductBrandDTO productBrandDTO,
                                                               @RequestParam(name = "productImages", required = false) List<MultipartFile> productImages,
                                                               @AuthenticationPrincipal MyUserDetails myUserDetails) throws IOException {

        MemberEntity member = myUserDetails.getMemberEntity(); // 현재 로그인한 사용자의 MemberEntity 가져오기

        if (member == null) {
            // 사용자 정보가 없는 경우 로그를 남깁니다.
            log.info("사용자 정보가 없습니다.");
        }

        // 카테고리 정보 작성 또는 가져오기
        ProductCategoryEntity productCategoryEntity = productUtilService.productCategoryWriteDetail(productCategoryDTO);
        // 브랜드 정보 작성 또는 가져오기
        ProductBrandEntity productBrandEntity = productUtilService.productBrandWriteDetail(productBrandDTO);
        // 상품글 작성
        ProductEntity productEntityWritePro = productService.productWriteDetail(productDTO, productCategoryEntity, productBrandEntity, member);
        // 이미지 저장
        productUtilService.saveProductImages(productEntityWritePro, productImages);

        Long productId = productEntityWritePro.getId(); // 작성한 글의 productId를 가져옴.

        Map<String,Object> response = new HashMap<>();
        response.put("status","success");
        response.put("redirectUrl","/product/"+productId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }



//    @GetMapping("{id}/list")
//    public String list(Model model,
//                       @PathVariable(name = "id") Long memberId,
//                       @RequestParam(name = "page", defaultValue = "1") int page,
//                       @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
//                       @RequestParam(name = "searchType", required = false) String searchType,
//                       @RequestParam(name = "searchKeyword", required = false) String searchKeyword) {
//
//        ProductService.ProductListResponse response = productService.getProductList(page, pageable, searchType, searchKeyword, memberId);
//        model.addAttribute("productList", response.getProductList());
//        model.addAttribute("nowPage", response.getNowPage());
//        model.addAttribute("startPage", response.getStartPage());
//        model.addAttribute("endPage", response.getEndPage());
//        model.addAttribute("totalPage", response.getTotalPage());
//        model.addAttribute("searchType", response.getSearchType());
//        model.addAttribute("searchKeyword", response.getSearchKeyword());
//
//        List<ProductImgDTO> productImgDTOS = productUtilService.getMainProductImage(response.getProductList().getContent());
//        model.addAttribute("productImages", productImgDTOS);
//
//        return "/product/list";
//    }


    
    // 송원철 / header에 로그인한 member정보 담아줌
    @GetMapping("/list")
    public String list(Model model, @AuthenticationPrincipal MyUserDetails myUserDetails){

        if (myUserDetails != null) {

            MemberDto member = memberService.detailMember(myUserDetails.getMemberEntity().getMemberId());
            String memberImageUrl = imageService.findImage(member.getMemberEmail()).getImageUrl();

            model.addAttribute("member", member);
            model.addAttribute("memberImageUrl", memberImageUrl);
        }

        Optional<Long> lastProductId = productUtilService.findLastProductId();
        model.addAttribute("lastProductId_from_server", lastProductId.orElse(null));
        return "product/list";
    }



    // 송원철 - member, semiMember 추가
    // 로그인 안한 사람, member, semiMember 모두 접근 가능
    @GetMapping("/{id}")
    public String getProductDetail(@PathVariable Long id, Model model,
                                   @AuthenticationPrincipal MyUserDetails myUserDetails,
                                   @AuthenticationPrincipal SemiMyUserDetails semiMyUserDetails) {
        // updateHits 메소드를 호출, 해당 게시글의 조회수를 하나 올린다.
        productUtilService.updateHits(id);
        ProductDTO productDTOViewDetail = productService.productViewDetail(id);
        if (productDTOViewDetail == null) {
            log.warn("productDTOViewDetail is null");
        } else {
            log.info("productDTOViewDetail: {}", productDTOViewDetail.toString());
            if (productDTOViewDetail.getProductCategory() == null) {
                log.warn("productDTOViewDetail's productCategory is null");
            }
        }
        List<ProductImgDTO> productImgDTOS = productUtilService.getProductImagesByProductId(id);


        MemberEntity member = myUserDetails != null ? myUserDetails.getMemberEntity() : null;
        SemiMemberEntity semiMember = semiMyUserDetails != null ? semiMyUserDetails.getSemiMemberEntity() : null;

        model.addAttribute("product", productDTOViewDetail);
        model.addAttribute("productImages", productImgDTOS);

        // 추가: member가 null이 아닌 경우에만 memberId를 model에 추가
        if (member != null) {
            MemberEntity loginMember = memberService.findMember(member.getMemberId());
            String memberImageUrl = imageService.findImage(member.getMemberEmail()).getImageUrl(); // header 정보 가져오기

            int cartCount = 0;
            CartEntity memberCart = cartService.findMemberCart(loginMember.getMemberId());
            List<CartItemEntity> cartItems = cartService.allMemberCartView(memberCart);

            for(CartItemEntity cartItem : cartItems) {
                cartCount += cartItem.getCartItemCount();
            }
            model.addAttribute("cartCount", cartCount);
            model.addAttribute("member", member);
            model.addAttribute("loginMember", loginMember); // header 정보 가져오기
            model.addAttribute("memberImageUrl", memberImageUrl); // header 정보 가져오기

            log.info("Member: {}", member.getMemberId());
        }else {
            log.info("Member is null");
        }

        // 추가: semiMember가 null이 아닌 경우에만 semiMemberId를 model에 추가
        if (semiMember != null) {
            SemiMemberEntity loginSemiMember = semiMemberService.findSemiMember(semiMember.getSemiMemberId());

            int cartCount = 0;
            CartEntity semiMemberCart = semiCartService.findSemiMemberCart(loginSemiMember.getSemiMemberId());
            List<CartItemEntity> cartItems = semiCartService.allSemiMemberCartView(semiMemberCart);

            for(CartItemEntity cartItem : cartItems) {
                cartCount += cartItem.getCartItemCount();
            }
            model.addAttribute("cartCount", cartCount);
            model.addAttribute("semiMember", semiMember);
            log.info("SemiMember: {}", semiMember.getSemiMemberId());
        }else {
            log.info("SemiMember is null");
        }
        return "product/detail";
    }



    // 송원철 / 헤더 정보 담기
    @GetMapping("/update/{id}")
    public String getProductUpdate(@PathVariable Long id, Model model, @AuthenticationPrincipal MyUserDetails myUserDetails) {

        if (myUserDetails != null) {

            MemberDto member = memberService.detailMember(myUserDetails.getMemberEntity().getMemberId());
            String memberImageUrl = imageService.findImage(member.getMemberEmail()).getImageUrl();

            model.addAttribute("member", member);
            model.addAttribute("memberImageUrl", memberImageUrl);
        }

        ProductDTO productDTOViewDetail = productService.productViewDetail(id);
        if (productDTOViewDetail == null) {
            log.warn("productDTOViewDetail is null");
        } else {
            log.info("productDTOViewDetail: {}", productDTOViewDetail.toString());
            if (productDTOViewDetail.getProductCategory() == null) {
                log.warn("productDTOViewDetail's productCategory is null");
            }
        }
        List<ProductImgDTO> productImgDTOS = productUtilService.getProductImagesByProductId(id);

        model.addAttribute("product", productDTOViewDetail);
        model.addAttribute("productImages", productImgDTOS);

        return "product/update";
    }



    @PostMapping("/update")
    public ResponseEntity<Map<String,Object>> postProductUpdate(@ModelAttribute ProductDTO productDTO,
                                                               @ModelAttribute ProductCategoryDTO productCategoryDTO,
                                                               @ModelAttribute ProductBrandDTO productBrandDTO,
                                                               @RequestParam(name = "productImages", required = false) List<MultipartFile> productImages,
                                                               @RequestParam(name = "productImagesOrder", required = false) String productImagesOrder)
                                                               throws IOException {

        // 상품글 수정
        ProductEntity productEntityUpdatePro = productService.productUpdateDetail(productDTO, productCategoryDTO, productBrandDTO);

        // 수정페이지에서 삭제한 이미지 삭제
        productUtilService.deleteProductImages(productEntityUpdatePro, productImagesOrder);

        // 이미지 수정 및 저장
        productUtilService.updateProductImages(productEntityUpdatePro, productImages, productImagesOrder);

        Long productId = productEntityUpdatePro.getId(); // 작성한 글의 productId를 가져옴.

        Map<String,Object> response = new HashMap<>();
        response.put("status","success");
        response.put("redirectUrl","/product/"+productId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }



    // DELETE (DELETE)
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        productService.delete(id);
        return "redirect:/product/list";
    }



    // 송원철 - 상품관리 페이지
    @GetMapping("/manage")
    public String getProductManage(){
        return "product/manage";
    }

}




