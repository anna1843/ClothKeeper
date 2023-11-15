package org.project.dev.review.dto;

import lombok.*;
import org.project.dev.product.entity.ProductEntity;
import org.project.dev.review.entity.ReviewEntity;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDto {

    private Long id;

    private String review;

    private String reviewWriter;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    // 덧글 작성시 board_id;
    private Long productId;


}
