package org.project.dev.review.entity;

import lombok.*;
import org.project.dev.product.entity.ProductEntity;
import org.project.dev.review.dto.ReviewDto;
import org.project.dev.utils.BaseEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Getter
@Setter
@Table(name = "product_review")
public class ReviewEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @Column(nullable = false, name = "review_content")
    private String review;

    @Column(name = "product_review_writer", nullable = false)
    private String reviewWriter;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private ProductEntity productEntity;

}
