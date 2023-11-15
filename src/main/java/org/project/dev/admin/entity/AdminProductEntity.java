package org.project.dev.admin.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.project.dev.admin.dto.AdminProductDto;
import org.project.dev.utils.BaseEntity;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "admin_product_table")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminProductEntity extends BaseEntity {

    /*
    Todo
     1. catfather49@gmail.com
     2.
     3.
     4.
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "product_color", nullable = false)
    private String productColor;

    @Column(name = "product_size", nullable = false)
    private String productSize;

    @Column(name = "product_description", length = 500, nullable = false)
    private String productDescription;

    @Column(name = "product_hits")
    private int productHits;

    // 이 부분을 나중에 member 쪽에 연결(?) 하면 될 것 같습니다.
    @Column(name = "product_writer", length = 20, nullable = false)
    private String productWriter;

    @ColumnDefault("true")
    private Boolean productDisplay;

    public static AdminProductEntity toEntity(AdminProductDto adminProductDto) {
        return AdminProductEntity.builder()
                .id(adminProductDto.getId())
                .productColor(adminProductDto.getProductColor())
                .productDescription(adminProductDto.getProductDescription())
                .productDisplay(adminProductDto.getProductDisplay())
                .productHits(adminProductDto.getProductHits())
                .productName(adminProductDto.getProductName())
                .productSize(adminProductDto.getProductSize())
                .productWriter(adminProductDto.getProductWriter())
                .build();
    }
}


