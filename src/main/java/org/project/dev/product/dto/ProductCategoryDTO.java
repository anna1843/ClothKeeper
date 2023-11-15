package org.project.dev.product.dto;

import lombok.*;
import org.project.dev.product.entity.ProductCategoryEntity;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategoryDTO {

    /*
    Todo
     1. code.aiaru@gmail.com
     2. 상품 브랜드 관련 DTO 입니다.
     3. x
     4. x
     */

    private Long id;
    private String productCategoryName;


    public static ProductCategoryDTO toDTO(ProductCategoryEntity productCategoryEntity){
        ProductCategoryDTO productCategoryDTO = new ProductCategoryDTO();
        productCategoryDTO.setId(productCategoryEntity.getId());
        productCategoryDTO.setProductCategoryName(productCategoryEntity.getProductCategoryName());

        return productCategoryDTO;
    }

}
