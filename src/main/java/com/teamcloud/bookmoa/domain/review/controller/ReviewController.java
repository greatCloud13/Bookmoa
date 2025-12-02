package com.teamcloud.bookmoa.domain.review.controller;

import com.teamcloud.bookmoa.domain.review.dto.ReviewRequest;
import com.teamcloud.bookmoa.domain.review.dto.ReviewResponse;
import com.teamcloud.bookmoa.domain.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewResponse> createReview(@RequestBody ReviewRequest request){
        ReviewResponse response = reviewService.createReview(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ReviewResponse>> getReviewsByIsbn(@RequestParam String isbn){
        List<ReviewResponse> reviews = reviewService.getReviewByIsbn(isbn);
        return ResponseEntity.ok(reviews);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewResponse> updateReview(
            @PathVariable Long id,
            @RequestBody ReviewRequest request){
        ReviewResponse response = reviewService.updateReview(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
}
