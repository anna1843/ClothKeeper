package org.project.dev.product.service;

import lombok.RequiredArgsConstructor;
import org.project.dev.product.dto.ProductDTO;
import org.project.dev.product.entity.ProductEntity;
import org.project.dev.product.repository.ProductRepository;
import org.project.dev.product.repository.ProductSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductPaginationService {

    private final ProductRepository productRepository;


    // LIST with search & pagination (READ)
    // 검색어 없이 리스트 출력
    public Page<ProductDTO> productNoSearchList(Pageable pageable, Long memberId) {
        Specification<ProductEntity> entitySpecification = Specification.where(ProductSpecification.isDisplayTrue()
                .and(ProductSpecification.isMember(memberId)));
        Page<ProductEntity> productEntities = productRepository.findAll(entitySpecification,pageable);
        return productEntities.map(ProductDTO::toDTO);
    }

    // 검색어를 통한 리스트 출력
    public Page<ProductDTO> productSearchList(String searchType, String searchKeyword, Pageable pageable) {
        // DB의 productDisplay 값이 true 인 경우에만 보이도록 처리. (ADMIN/SELLER의 경우에는 false여도 보이도록 처리)
        Specification<ProductEntity> entitySpecification = Specification.where(ProductSpecification.isDisplayTrue());

        // 검색타입 추가 시, 이 곳에 위치하게 됩니다.
        switch (searchType) {
            case "productNameContains":
                entitySpecification = entitySpecification
                        .and(ProductSpecification.productNameContains(searchKeyword));
                break;
            case "productDescriptionContains":
                entitySpecification = entitySpecification
                        .and(ProductSpecification.productDescriptionContains(searchKeyword));
                break;
        }

        Page<ProductEntity> productEntities = productRepository.findAll(entitySpecification, pageable);
        return productEntities.map(ProductDTO::toDTO); // Entity를 DTO로 변환
    }

    // 페이징처리 : 시작 페이지 계산 로직
    public static int calculateStartPage(int nowPage, int totalPage) {
        int startPage = Math.max(nowPage - 4, 1);
        if (totalPage - startPage < 8) {
            startPage = Math.max(totalPage - 8, 1);
        }
        return startPage;
    }

    // 페이징처리 : 마지막 페이지 계산 로직
    public static int calculateEndPage(int nowPage, int totalPage) {
        int endPage = Math.min(nowPage + 4, totalPage);
        if (endPage - calculateStartPage(nowPage, totalPage) < 8) {
            endPage = Math.min(calculateStartPage(nowPage, totalPage) + 8, totalPage);
        }
        return endPage;
    }


}
