package com.teamcloud.bookmoa.domain.review.dto;

import com.teamcloud.bookmoa.domain.review.entity.Review;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ReviewResponse {

    private Long id;
    private String isbn;
    private String bookTitle;
    private String reviewer;
    private String content;
    private Integer rating;
    private LocalDateTime createdAt;

    public static ReviewResponse from(Review review){
        return ReviewResponse.builder()
                .id(review.getId())
                .isbn(review.getBook().getIsbn())
                .bookTitle(review.getBook().getTitle())
                .reviewer(review.getReviewer())
                .content(review.getContent())
                .rating(review.getRating())
                .createdAt(review.getCreatedAt())
                .build();
    }

}
