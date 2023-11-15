package org.project.dev.product.dto;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.project.dev.member.entity.MemberEntity;
import org.project.dev.product.entity.ProductBrandEntity;
import org.project.dev.product.entity.ProductEntity;

import javax.persistence.Column;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProductBrandDTO {

    /*
    Todo
     1. code.aiaru@gmail.com
     2. 상품 브랜드 관련 DTO 입니다.
     3. x
     4. x
     */

    private Long id;
    private String productBrandName;


    public static ProductBrandDTO toDTO(ProductBrandEntity productBrandEntity){
        ProductBrandDTO productBrandDTO = new ProductBrandDTO();
        productBrandDTO.setId(productBrandEntity.getId());
        productBrandDTO.setProductBrandName(productBrandEntity.getProductBrandName());

        return productBrandDTO;
    }

}
