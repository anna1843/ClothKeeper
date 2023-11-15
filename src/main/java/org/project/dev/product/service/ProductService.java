package org.project.dev.product.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.project.dev.member.entity.MemberEntity;
import org.project.dev.member.repository.MemberRepository;
import org.project.dev.product.dto.ProductBrandDTO;
import org.project.dev.product.dto.ProductCategoryDTO;
import org.project.dev.product.dto.ProductDTO;
import org.project.dev.product.entity.ProductBrandEntity;
import org.project.dev.product.entity.ProductCategoryEntity;
import org.project.dev.product.entity.ProductEntity;
import org.project.dev.product.repository.ProductBrandRepository;
import org.project.dev.product.repository.ProductCategoryRepository;
import org.project.dev.product.repository.ProductRepository;
import org.project.dev.product.repository.ProductSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    /*
    Todo
     1. code.aiaru@gmail.com
     2. CRUD 처리를 위한 로직들이 모인 곳입니다.
     3. x
     4. x
     */


    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductBrandRepository productBrandRepository;
    private final ProductPaginationService productPaginationService;
    private final ProductUtilService productUtilService;
    private final MemberRepository memberRepository; // 송원철


    // WRITE (CREATE)
//    @Transactional
//    public ProductEntity productWriteDetail(ProductDTO productDTO){
//        productDTO.setProductHits(0); // productHits 초기화
//        ProductEntity productEntity = ProductEntity.toEntity(productDTO);
//        return productRepository.save(productEntity);
//    }

    // 송원철 / write 시 memberId 저장
    @Transactional
    public ProductEntity productWriteDetail(ProductDTO productDTO,
                                            ProductCategoryEntity productCategoryEntity,
                                            ProductBrandEntity productBrandEntity,
                                            MemberEntity memberEntity){
        productDTO.setProductHits(0); // productHits 초기화
        ProductEntity productEntity = ProductEntity.toEntity(productDTO);
        productEntity.setProductCategoryEntity(productCategoryEntity);
        productEntity.setProductBrandEntity(productBrandEntity);
        productEntity.setMember(memberEntity); // 현재 로그인한 사용자의 MemberEntity 설정
        return productRepository.save(productEntity);
    }




    // LIST (READ)
    public ProductListResponse getProductList(int page, Pageable pageable,
                                              String searchType, String searchKeyword,Long memberId) {
        Pageable adjustedPageable = PageRequest.of(page - 1, pageable.getPageSize(), pageable.getSort());

        Page<ProductDTO> productList;

        if (searchKeyword == null || searchType == null) {
            productList = productPaginationService.productNoSearchList(adjustedPageable, memberId);
        } else {
            productList = productPaginationService.productSearchList(searchType, searchKeyword, adjustedPageable);
        }

        int nowPage = productList.getPageable().getPageNumber() + 1;
        int totalPage = productList.getTotalPages();
        int startPage = productPaginationService.calculateStartPage(nowPage, totalPage);
        int endPage = productPaginationService.calculateEndPage(nowPage, totalPage);

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


    // 스크롤 페이징 처리 중 =======================================================
    @Transactional(readOnly = true)
    public List<ProductDTO> getProductInfoCollected(Long lastProductId, int limit,
                                                    String searchKeyword,
                                                    String[] categories,
                                                    String[] brands,
                                                    String[] colors) {

        Specification<ProductEntity> spec = Specification
                .where(ProductSpecification.isDisplayTrue())    // productDisplay 값이 true 인 경우
                .and(ProductSpecification.isIdLessThan(lastProductId)); // lastProductId 보다 id 값이 작은 경우

        // 여기에 필터 요소를 추가.
        if(searchKeyword != null) {
            spec = spec.and(ProductSpecification.productNameContains(searchKeyword));
        }

        // 필터 요소가 하나가 아닐 경우를 대비해서 반복문 사용.

        // 카테고리 필터링
        if(categories != null) {
            Specification<ProductEntity> categorySpec = null;
            for(String category : categories) {
                if(categorySpec == null) {
                    categorySpec = ProductSpecification.productCategoryContains(category);
                } else {
                    categorySpec = categorySpec.or(ProductSpecification.productCategoryContains(category));
                }
            }
            if(categorySpec != null) {
                spec = spec.and(categorySpec);
            }
        }
        // 브랜드 필터링
        if(brands != null) {
            Specification<ProductEntity> brandSpec = null;
            for(String brand : brands) {
                if(brandSpec == null) {
                    brandSpec = ProductSpecification.productBrandContains(brand);
                } else {
                    brandSpec = brandSpec.or(ProductSpecification.productBrandContains(brand));
                }
            }
            if(brandSpec != null) {
                spec = spec.and(brandSpec);
            }
        }
        // 색상 필터링
        if(colors != null) {
            Specification<ProductEntity> colorSpec = null;
            for(String color : colors) {
                if(colorSpec == null) {
                    colorSpec = ProductSpecification.productColorContains(color);
                } else {
                    colorSpec = colorSpec.or(ProductSpecification.productColorContains(color));
                }
            }
            if(colorSpec != null) {
                spec = spec.and(colorSpec);
            }
        }



        Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "id"));
        // lastProductId보다 작은 ID를 가진 제품을 찾아서 limit 개수만큼 반환
        Page<ProductEntity> productEntityPage = productRepository.findAll(spec, pageable);
        // Page 객체에서 List 객체를 가져옴
        List<ProductEntity> productEntities = productEntityPage.getContent();

//        System.out.println("Product Entities: " + productEntities);

        // DTO로 변환
        List<ProductDTO> productDTOs = productEntities.stream()
                .map(ProductDTO::toDTO)
                .collect(Collectors.toList());

//        System.out.println("Product DTOs: " + productDTOs);
//        System.out.println(brands);
//        System.out.println(colors);

        return productDTOs;
    }


    // ==========================================================================

    // 송원철 / 상품 개별 불러오기
    public ProductEntity productView(Long productId) {
        return productRepository.findById(productId).get();
    }


    // DETAIL (SELECT) & UPDATE (UPDATE) & UPDATE PROCESS (UPDATE)
    @Transactional(readOnly = true)
    public ProductDTO productViewDetail(Long id) {
        // 게시물의 ID를 이용하여 해당 게시물을 데이터베이스에서 찾음
        Optional<ProductEntity> optionalProductEntity = productRepository.findById(id);
        // 만약 해당 ID에 해당하는 게시물이 존재한다면,
        if(optionalProductEntity.isPresent()){
            // Optional 객체에서 실제 ProductEntity 객체를 가져옴
            ProductEntity productEntity = optionalProductEntity.get();
            // 가져온 ProductEntity 객체를 ProductDTO 객체로 변환
            ProductDTO productDTO = ProductDTO.toDTO(productEntity);
            // 변환된 ProductDTO 객체를 반환
            return productDTO;
        }else{
            // 해당 ID에 해당하는 게시물이 존재하지 않을 경우 null 반환
            return null;
        }
    }

    // UPDATE PROCESS (UPDATE)
//    @Transactional
//    public ProductDTO productUpdateDetail(ProductDTO productDTO) {
//        ProductEntity productEntity = ProductEntity.toEntity(productDTO);
//        productRepository.save(productEntity);
//        return productViewDetail(productDTO.getId());
//    }


    // Update 수정중==================================================================

    @Transactional
    public ProductEntity productUpdateDetail(ProductDTO productDTO,
                                             ProductCategoryDTO productCategoryDTO,
                                             ProductBrandDTO productBrandDTO){
//        ProductEntity productEntity = ProductEntity.toEntity(productDTO);
//        productEntity.setProductCategoryEntity(productCategoryEntity);
//        productEntity.setProductBrandEntity(productBrandEntity);
//        return productRepository.save(productEntity);
        Optional<ProductEntity> existingProduct = productRepository.findById(productDTO.getId());


        ProductEntity productEntity = existingProduct.get();

        // 필드 업데이트
        productEntity.updateFromDTO(productDTO);

        // 새로운 또는 기존의 ProductCategoryEntity와 ProductBrandEntity를 찾거나 생성
        ProductCategoryEntity newOrExistingCategory = productUtilService.productCategoryWriteDetail(productCategoryDTO);
        ProductBrandEntity newOrExistingBrand = productUtilService.productBrandWriteDetail(productBrandDTO);

        // 필요한 필드만 업데이트
        productEntity.setProductCategoryEntity(newOrExistingCategory);
        productEntity.setProductBrandEntity(newOrExistingBrand);

        return productRepository.save(productEntity);
    }


    // ==============================================================================


    // DELETE (DELETE)
    // 이름은 delete이지만, 실제 로직은 update.
    // productDisplay 값을 false로 바꿔, 사용자에게 보이지 않도록 처리합니다.
    @Transactional
    public void delete(Long id) {
        productRepository.softDelete(id);
    }


}