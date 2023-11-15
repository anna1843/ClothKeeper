package org.project.dev.product.controller;

import lombok.RequiredArgsConstructor;
import org.project.dev.product.dto.ProductBrandDTO;
import org.project.dev.product.dto.ProductCategoryDTO;
import org.project.dev.product.dto.ProductDTO;
import org.project.dev.product.entity.ProductBrandEntity;
import org.project.dev.product.entity.ProductCategoryEntity;
import org.project.dev.product.repository.ProductBrandRepository;
import org.project.dev.product.repository.ProductCategoryRepository;
import org.project.dev.product.repository.ProductRepository;
import org.project.dev.product.service.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class ProductRestController {

    private final ProductCategoryRepository productCategoryRepository;
    private final ProductBrandRepository productBrandRepository;
    private final ProductRepository productRepository;
    private final ProductService productService;


    // 브랜드 이름 자동 완성 반환.
    @GetMapping("/categoryName")
    public List<ProductCategoryDTO> categoryNameAutocomplete(@RequestParam("keyword") String keyword){
        try {
            List<ProductCategoryEntity> entities =
                    productCategoryRepository.findByProductCategoryNameContaining(keyword);
            List<ProductCategoryDTO> dtos = entities.stream()
                    .map(ProductCategoryDTO::toDTO)
                    .collect(Collectors.toList());
            return dtos;
        } catch (Exception e) {
            // 예외 처리
            return Collections.emptyList();
        }
    }

    // 브랜드 이름 자동 완성 반환.
    @GetMapping("/brandName")
    public List<ProductBrandDTO> brandNameAutocomplete(@RequestParam("keyword") String keyword){
        try {
            List<ProductBrandEntity> entities =
                    productBrandRepository.findByProductBrandNameContaining(keyword);
            List<ProductBrandDTO> dtos = entities.stream()
                    .map(ProductBrandDTO::toDTO)
                    .collect(Collectors.toList());
            return dtos;
        } catch (Exception e) {
            // 예외 처리
            return Collections.emptyList();
        }
    }

    // 스크롤 페이징 처리 작업 중 ========================================================
    // Scroll List
    @GetMapping("/scrollList")
    public List<ProductDTO> getProductListCollected(
            @RequestParam(required = false, defaultValue = "0") Long lastProductId,
            @RequestParam(required = false, defaultValue = "10") int limit,
            @RequestParam(required = false) String searchKeyword,
            @RequestParam(required = false) String[] categories,
            @RequestParam(required = false) String[] brands,
            @RequestParam(required = false) String[] colors) {

        // 서비스 메서드 호출
        List<ProductDTO> productDTOs = productService
                .getProductInfoCollected(lastProductId, limit, searchKeyword, categories, brands, colors);

        return productDTOs;
    }

    // ================================================================================


}
