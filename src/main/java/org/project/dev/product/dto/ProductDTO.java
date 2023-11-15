package org.project.dev.product.dto;

import lombok.*;
import org.project.dev.member.entity.MemberEntity;
import org.project.dev.product.entity.ProductBrandEntity;
import org.project.dev.product.entity.ProductEntity;
import org.project.dev.product.entity.ProductImgEntity;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    /*
    Todo
     1. code.aiaru@gmail.com
     2. 상품 관련 DTO 입니다.
     3. x
     4. 1) CreateDate와 UpdateDate는 org.project.dev.utils.BaseEntity 상속을 통해 받았습니다.
        2) 판매글 작성자는 일단 productWriter로 해놨습니다. 추후에 member 쪽과 연동을 하면 될 것 같습니다.
        2023-08-23 : productDisplay -> isProductDisplayed 변경하였습니다.
     */

    private Long id;
    private String productName;
    private String productColor;
    private String productSize;
    private int productStock;
    private String productDescription;
    private int productHits;
    private Long productPrice;
    private Boolean isProductDisplayed;

    private LocalDateTime productCreateTime;
    private LocalDateTime productUpdateTime;

    private ProductBrandDTO productBrand;
    private ProductCategoryDTO productCategory;
    private List<ProductImgDTO> productImgDTOS;

    // 송원철 / 장바구니 관련
    private MemberEntity member;
    // 송원철 / memberNickName 출력 용도
    private String memberNickName;



    public static ProductDTO toDTO(ProductEntity productEntity){
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(productEntity.getId());
        productDTO.setProductName(productEntity.getProductName());
        productDTO.setProductColor(productEntity.getProductColor());
        productDTO.setProductSize(productEntity.getProductSize());
        productDTO.setProductStock(productEntity.getProductStock());
        productDTO.setProductDescription(productEntity.getProductDescription());
        productDTO.setProductHits(productEntity.getProductHits());
        productDTO.setProductPrice(productEntity.getProductPrice());
        productDTO.setIsProductDisplayed(productEntity.getIsProductDisplayed());
        productDTO.setProductCreateTime(productEntity.getCreateTime());
        productDTO.setProductUpdateTime(productEntity.getUpdateTime());
        productDTO.setMemberNickName(productEntity.getMember().getMemberNickName());

        // ProductBrandEntity를 ProductBrandDTO로 변환
        if (productEntity.getProductBrandEntity() != null) {
            productDTO.setProductBrand(ProductBrandDTO.toDTO(productEntity.getProductBrandEntity()));
        }

        if (productEntity.getProductCategoryEntity() != null) {
            productDTO.setProductCategory(ProductCategoryDTO.toDTO(productEntity.getProductCategoryEntity()));
        }

        // ProductImgEntity 리스트를 ProductImgDTO 리스트로 변환
        if (productEntity.getProductImgEntities() != null && !productEntity.getProductImgEntities().isEmpty()) {
            List<ProductImgDTO> imgDTOs = productEntity.getProductImgEntities().stream()
                    .map(ProductImgDTO::toDTO)
                    .collect(Collectors.toList());
            productDTO.setProductImgDTOS(imgDTOs);
        }
        return productDTO;
    }

}
