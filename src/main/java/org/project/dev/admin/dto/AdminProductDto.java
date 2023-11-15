package org.project.dev.admin.dto;


import lombok.*;
import org.project.dev.admin.entity.AdminProductEntity;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminProductDto {

    private Long id;
    private String productName;
    private String productColor;
    private String productSize;
    private String productDescription;
    private int productHits;
    private String productWriter;
    private Boolean productDisplay;
    private LocalDateTime productCreateDate;
    private LocalDateTime productUpdateDate;

    public static AdminProductDto toDto(AdminProductEntity adminProductEntity){
        return AdminProductDto.builder()
                .id(adminProductEntity.getId())
                .productColor(adminProductEntity.getProductColor())
                .productDisplay(adminProductEntity.getProductDisplay())
                .productCreateDate(adminProductEntity.getCreateTime())
                .productDescription(adminProductEntity.getProductDescription())
                .productHits(adminProductEntity.getProductHits())
                .productName(adminProductEntity.getProductName())
                .productSize(adminProductEntity.getProductSize())
                .productUpdateDate(adminProductEntity.getUpdateTime())
                .productWriter(adminProductEntity.getProductWriter())
                .build();
    }

}
