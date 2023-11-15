package org.project.dev.review.service;

import lombok.RequiredArgsConstructor;
import org.project.dev.config.member.MyUserDetails;
import org.project.dev.inquiryReply.dto.ReplyDto;
import org.project.dev.inquiryReply.entity.ReplyEntity;
import org.project.dev.notice.entity.InquiryEntity;
import org.project.dev.product.entity.ProductEntity;
import org.project.dev.product.repository.ProductRepository;
import org.project.dev.review.dto.ReviewDto;
import org.project.dev.review.entity.ReviewEntity;
import org.project.dev.review.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    private final ProductRepository productRepository;

    public ReviewDto reviewWrite(ReviewDto replyDto, MyUserDetails myUserDetails) {

        ProductEntity productEntity
                = productRepository.findById(replyDto.getProductId()).orElseThrow(() -> new IllegalArgumentException("X"));

        ReviewEntity reviewEntity = ReviewEntity.builder()
                .review(replyDto.getReview())
                .reviewWriter(myUserDetails.getMemberEntity().getMemberEmail())
                .productEntity(productEntity)
                .build();
        Long reviewId = reviewRepository.save(reviewEntity).getId();

        ReviewEntity reviewEntity1 = reviewRepository.findById(reviewId).orElseThrow(() -> new IllegalArgumentException("X"));

        return ReviewDto.builder()
                .id(reviewEntity1.getId())
                .review(reviewEntity1.getReview())
                .reviewWriter(reviewEntity1.getReviewWriter())
                .updateTime(reviewEntity1.getUpdateTime())
                .createTime(reviewEntity1.getCreateTime())
                .productId(reviewEntity1.getProductEntity().getId())
                .build();
    }


    public List<ReviewDto> reviewList(ReviewDto reviewDto) {

        ProductEntity productEntity
                = productRepository.findById(reviewDto.getProductId()).orElseThrow(() -> {
            throw new IllegalArgumentException("X");
        });
        List<ReviewDto> reviewDtos = new ArrayList<>();
        List<ReviewEntity> reviewEntities
                = reviewRepository.findByProductId(productEntity.getId());

        for (ReviewEntity reviewEntity : reviewEntities) {

            reviewDtos.add(ReviewDto.builder()
                    .id(reviewEntity.getId())
                    .review(reviewEntity.getReview())
                    .reviewWriter(reviewEntity.getReviewWriter())
                    .productId(reviewEntity.getProductEntity().getId())
                    .createTime(reviewEntity.getCreateTime())
                    .updateTime(reviewEntity.getUpdateTime())
                    .build());
        }
        return reviewDtos;
    }

    public int replyDelete(Long id) {

        Optional<ReviewEntity> reviewEntity
                = Optional.ofNullable(reviewRepository.findById(id).orElseThrow(() -> {
            throw new IllegalArgumentException("X");
        }));
        if (reviewEntity.isPresent()) {
            reviewRepository.delete(reviewEntity.get());
            return 1;
        }
        return 0;
    }


    public int reviewUp(ReviewDto reviewDto, MyUserDetails myUserDetails) {

        ProductEntity productEntity
                = productRepository.findById(reviewDto.getProductId()).orElseThrow(()->{
                    throw new IllegalArgumentException("X");
        });
        ReviewEntity reviewEntity = ReviewEntity.builder()
                .id(reviewDto.getId())
                .review(reviewDto.getReview())
                .reviewWriter(myUserDetails.getMemberEntity().getMemberEmail())
                .productEntity(productEntity)
                .build();
        Long saveId = reviewRepository.save(reviewEntity).getId();

        Optional<ReviewEntity> optionalReviewEntity
                = Optional.ofNullable(reviewRepository.findById(saveId).orElseThrow(()->{
                    throw new IllegalArgumentException("X");
        }));
        if (optionalReviewEntity.isPresent()){
            return 1;
        }
        return 0;
    }
}
