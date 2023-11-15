package org.project.dev.product.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.project.dev.utils.BaseEntity;


import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "product_img_table") // 이미지 저장을 위한 테이블
public class ProductImgEntity extends BaseEntity {

    /*
   Todo
    1. code.aiaru@gmail.com
    2. 상품 이미지 관련 Entity 입니다.
    3. x
    4. CreateDate와 UpdateDate는 org.project.dev.utils.BaseEntity 상속을 통해 받았습니다.
    */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_img_id")
    private Long id;

    @Column(name = "product_img_original_name", nullable = false)
    private String productImgOriginalName;

    @Column(name = "product_img_saved_name", nullable = false)
    private String productImgSavedName;

    @Column(name = "product_img_saved_path", nullable = false)
    private String productImgSavedPath;

    @Column(name = "product_img_Order", nullable = false)
    private int productImgOrder;

    @Column(name = "is_product_img_displayed", nullable = false)
    @ColumnDefault("false")
    private Boolean isProductImgDisplayed;


    // DB 연관관계 설정 -> ProductEntity
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id") // Foreign Key
    private ProductEntity productEntity;



}