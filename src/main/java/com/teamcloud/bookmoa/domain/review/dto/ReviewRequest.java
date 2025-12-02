package com.teamcloud.bookmoa.domain.review.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewRequest {

    private String isbn;
    private String reviewer;
    private String content;
    private Integer rating;

}
