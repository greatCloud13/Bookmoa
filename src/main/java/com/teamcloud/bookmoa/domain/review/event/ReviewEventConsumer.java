package com.teamcloud.bookmoa.domain.review.event;

import com.teamcloud.bookmoa.domain.book.entity.Book;
import com.teamcloud.bookmoa.domain.book.repository.BookRepository;
import com.teamcloud.bookmoa.domain.review.entity.Review;
import com.teamcloud.bookmoa.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReviewEventConsumer {

    private final BookRepository bookRepository;
    private final ReviewRepository reviewRepository;

    @RabbitListener(queues = "review.statistics.queue")
    @Transactional
    @CacheEvict(value = "book", key = "#event.isbn")
    public void handleReviewEvent(ReviewEvent event){
        log.info("이벤트 수신 - ISBN: {}, EventType: {}", event.getIsbn(), event.getEventType());

        Book book = bookRepository.findById(event.getIsbn())
                .orElseThrow(()-> new IllegalArgumentException("책을 찾을 수 없습니다."));

        // 해당 책의 모든 리뷰 조회
        List<Integer> ratings = reviewRepository.findByBookIsbn(event.getIsbn())
                .stream()
                .map(Review::getRating)
                .toList();

        // 통계 계산
        int reviewCount = ratings.size();

        double averageRating = reviewCount > 0 ?
                ratings.stream().mapToInt(Integer::intValue).average().orElse(0.0)
                : 0.0;

        // 반올림
        averageRating = Math.round(averageRating * 100.0)/ 100.0;

        // Book 업데이트
        book.updateStatistics(averageRating, reviewCount);

        log.info("통계 업데이트 완료 - ISBN: {}, 평균 평점: {}, 리뷰 수: {}",
                event.getIsbn(), averageRating, reviewCount);
    }

}
