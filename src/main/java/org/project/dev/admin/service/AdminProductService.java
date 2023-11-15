package org.project.dev.admin.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.project.dev.admin.repository.AdminProductRepository;
import org.project.dev.product.dto.ProductDTO;
import org.project.dev.product.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminProductService {

    private final AdminProductRepository adminProductRepository;

    private final AdminProductPaginationService adminProductPaginationService;


    /*
    TODO
    검색조건 달아야함
    그냥 다 땡겨옴 null 처리 안함
     */
    public List<ProductDTO> getProductList(int memberId) {
        List<ProductDTO> productDtoList = new ArrayList<>();
        List<ProductEntity> productEntityList = adminProductRepository.getMemberIdProductList(memberId);

        if (productEntityList.isEmpty()) {
            throw new IllegalArgumentException("가져올 상품이 없습니다");
        }

        for (ProductEntity productEntity : productEntityList) {
//            productDtoList.add(ProductDTO.toDTO(productEntity));

        }
        return productDtoList;
    }


    /*
    TODO
    단일조회
    */
    public ProductDTO getProductDetail(Long productId) {
        Optional<ProductEntity> productEntity = Optional.ofNullable(adminProductRepository.findById(productId).orElseThrow(() -> {
            throw new IllegalArgumentException("가져올 항목이 없습니다");
        }));

        return ProductDTO.toDTO(productEntity.get());
//        return null;
    }

    public ProductListResponse getProductList(int page, Pageable pageable,
                                                             String searchType, String searchKeyword, Long memberId) {
        Pageable adjustedPageable = PageRequest.of(page - 1, pageable.getPageSize(), pageable.getSort());

        Page<ProductDTO> productList;

        if (searchKeyword == null || searchType == null) {
            productList = adminProductPaginationService.productNoSearchList(adjustedPageable, memberId);
        } else {
            productList = adminProductPaginationService.productSearchList(searchType, searchKeyword, adjustedPageable);
        }

        int nowPage = productList.getPageable().getPageNumber() + 1;
        int totalPage = productList.getTotalPages();
        int startPage = adminProductPaginationService.calculateStartPage(nowPage, totalPage);
        int endPage = adminProductPaginationService.calculateEndPage(nowPage, totalPage);

        return new ProductListResponse(productList, nowPage, startPage, endPage, totalPage, searchType, searchKeyword);
    }

    @Data
    @AllArgsConstructor
    public class ProductListResponse {
        private Page<ProductDTO> productList;
        private int nowPage;
        private int startPage;
        private int endPage;
        private int totalPage;
        private String searchType;
        private String searchKeyword;
    }
}
