package com.teamcloud.bookmoa.domain.review.service;

import com.teamcloud.bookmoa.domain.book.entity.Book;
import com.teamcloud.bookmoa.domain.book.service.BookService;
import com.teamcloud.bookmoa.domain.review.dto.ReviewRequest;
import com.teamcloud.bookmoa.domain.review.dto.ReviewResponse;
import com.teamcloud.bookmoa.domain.review.entity.Review;
import com.teamcloud.bookmoa.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookService bookService;

    @Transactional
    public ReviewResponse createReview(ReviewRequest request){
        log.info("리뷰 등록 - ISBN: {}, 작성자: {}", request.getIsbn(), request.getReviewer());

        // 책이 DB에 없으면 알라딘 api 호출 후 저장
        Book book = bookService.getBookByIsbn(request.getIsbn());

        Review review = Review.builder()
                .book(book)
                .reviewer(request.getReviewer())
                .content(request.getContent())
                .rating(request.getRating())
                .build();

        Review savedReview = reviewRepository.save(review);

        return ReviewResponse.from(savedReview);
    }

    public List<ReviewResponse> getReviewByIsbn(String isbn){
        log.info("리뷰 목록 조회 - ISBN: {}", isbn);

        List<Review> reviews = reviewRepository.findByBookIsbn(isbn);

        return reviews.stream()
                .map(ReviewResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public ReviewResponse updateReview(Long id, ReviewRequest request){
        log.info("리뷰 수정 - ID: {}", id);

        Review review = reviewRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("리뷰를 찾을 수 없습니다."));

        review.update(request.getContent(), request.getRating());

        return ReviewResponse.from(review);
    }

    @Transactional
    public void deleteReview(Long id) {
        log.info("리뷰 삭제 - ID: {}", id);

        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다: " + id));

        reviewRepository.delete(review);
    }

}
