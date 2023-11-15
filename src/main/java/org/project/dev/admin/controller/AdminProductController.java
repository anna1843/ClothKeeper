package org.project.dev.admin.controller;

import lombok.RequiredArgsConstructor;
import org.project.dev.admin.service.AdminProductService;
import org.project.dev.config.member.MyUserDetails;
import org.project.dev.product.dto.ProductDTO;
import org.project.dev.product.dto.ProductImgDTO;
import org.project.dev.product.service.ProductService;
import org.project.dev.product.service.ProductUtilService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/admin/product")
@RequiredArgsConstructor
public class AdminProductController {

    private final AdminProductService adminProductService;


    private final ProductUtilService productUtilService;

    @GetMapping("{id}/list")
    public String list(Model model,
                       @PathVariable(name = "id") Long memberId,
                       @RequestParam(name = "page", defaultValue = "1") int page,
                       @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                       @RequestParam(name = "searchType", required = false) String searchType,
                       @RequestParam(name = "searchKeyword", required = false) String searchKeyword) {

        AdminProductService.ProductListResponse response = adminProductService.getProductList(page, pageable, searchType, searchKeyword, memberId);
        model.addAttribute("productList", response.getProductList());
        model.addAttribute("nowPage", response.getNowPage());
        model.addAttribute("startPage", response.getStartPage());
        model.addAttribute("endPage", response.getEndPage());
        model.addAttribute("totalPage", response.getTotalPage());
        model.addAttribute("searchType", response.getSearchType());
        model.addAttribute("searchKeyword", response.getSearchKeyword());

        List<ProductImgDTO> productImgDTOS = productUtilService.getMainProductImage(response.getProductList().getContent());
        model.addAttribute("productImages", productImgDTOS);

        return "admin/product/list";
    }

    @GetMapping("{productId}/detail")
    public String list(@PathVariable(name = "productId") Long productId,
                       @AuthenticationPrincipal MyUserDetails myUserDetails,
                       Model model) {
        ProductDTO productDetail = adminProductService.getProductDetail(productId);
        List<ProductImgDTO> productImgDTOS = productUtilService.getProductImagesByProductId(productId);
        model.addAttribute("productDetail", productDetail);
        model.addAttribute("productImgDTOS", productImgDTOS);
        return "admin/product/detail";
    }


}
