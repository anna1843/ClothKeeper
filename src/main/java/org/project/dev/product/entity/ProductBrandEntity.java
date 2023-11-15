package org.project.dev.product.entity;


import lombok.Getter;
import lombok.Setter;
import org.project.dev.product.dto.ProductBrandDTO;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Entity
@Table(name = "product_brand_table")
public class ProductBrandEntity {

    /*
    Todo
     1. code.aiaru@gmail.com
     2. 상품 브랜드 관련 Entity 입니다.
     3. x
     4. x
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_brand_id")
    private Long id;

    @Column(name = "product_brand_name", nullable = false)
    private String productBrandName;

    // DB 연관관계 설정 -> ProductEntity
    @OneToMany(mappedBy = "productBrandEntity", cascade = CascadeType.REMOVE)
    private List<ProductEntity> productEntities = new ArrayList<>();



    // Dto to Entity
    public static ProductBrandEntity toEntity(ProductBrandDTO productBrandDTO){
        ProductBrandEntity productBrandEntity = new ProductBrandEntity();
        productBrandEntity.setId(productBrandDTO.getId());
        productBrandEntity.setProductBrandName(productBrandDTO.getProductBrandName());

        return productBrandEntity;
    }

}
