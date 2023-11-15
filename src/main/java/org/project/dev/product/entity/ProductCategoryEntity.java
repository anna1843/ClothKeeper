package org.project.dev.product.entity;


import lombok.Getter;
import lombok.Setter;
import org.project.dev.product.dto.ProductCategoryDTO;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Entity
@Table(name = "product_category_table")
public class ProductCategoryEntity {

    /*
    Todo
     1. code.aiaru@gmail.com
     2. 상품 브랜드 관련 Entity 입니다.
     3. x
     4. x
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_category_id")
    private Long id;

    @Column(name = "product_category_name", nullable = false)
    private String productCategoryName;

    // DB 연관관계 설정 -> ProductEntity
    @OneToMany(mappedBy = "productCategoryEntity", cascade = CascadeType.REMOVE)
    private List<ProductEntity> productEntities = new ArrayList<>();



    // Dto to Entity
    public static ProductCategoryEntity toEntity(ProductCategoryDTO productCategoryDTO){
        ProductCategoryEntity productCategoryEntity = new ProductCategoryEntity();
        productCategoryEntity.setId(productCategoryDTO.getId());
        productCategoryEntity.setProductCategoryName(productCategoryDTO.getProductCategoryName());

        return productCategoryEntity;
    }

}
