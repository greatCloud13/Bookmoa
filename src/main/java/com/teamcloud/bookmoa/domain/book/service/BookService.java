package com.teamcloud.bookmoa.domain.book.service;

import com.teamcloud.bookmoa.domain.book.client.AladinApiClient;
import com.teamcloud.bookmoa.domain.book.dto.AladinApiResponse;
import com.teamcloud.bookmoa.domain.book.dto.AladinBookItem;
import com.teamcloud.bookmoa.domain.book.entity.Book;
import com.teamcloud.bookmoa.domain.book.repository.BookRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository bookRepository;
    private final AladinApiClient aladinApiClient;

    private static final int MIN_REVIEWS = 5; // 최소 리뷰 수 (베이지안 평균 계산에 이용함)

    @Cacheable(value = "bookSearch", key = "#query")
    public AladinApiResponse searchBooks(String query){
        log.info("도서 검색 - 검색어: {}", query);
        return aladinApiClient.searchBooks(query);
    }

    @Cacheable(value = "popularBooks")
    public List<Book> getPopularBooks(int limit){
        log.info("인기 도서 조회 - 개수: {}", limit);

        // 전체 평균 계산
        Double globalAvg = bookRepository.getGlobalAverageRating();

        // 리뷰가 0일 경우 빈 리스트 반환
        if(globalAvg == null){
            log.warn("리뷰가 있는 도서가 없습니다.");
            return List.of();
        }

        log.info("전체 평균 평점: {}, 최소 리뷰 수: {}", globalAvg, MIN_REVIEWS);

        return bookRepository.findPopularBooks(
                MIN_REVIEWS,
                globalAvg,
                PageRequest.of(0, limit)
        );
    }


    /**
     * ISBN을 통해 db 도서 조회
     * db에 해당 도서없을 시 API를 통해 조회
     * 저장에는 ISBN 13을 사용
     */
    @Cacheable(value = "book", key = "#isbn")
    public Book getBookByIsbn(String isbn){
        log.info("도서 상세 조회 - ISBN: {}", isbn);

        return bookRepository.findById(isbn)
                .orElseGet(()->fetchAndSave(isbn));
    }

    @Transactional
    private Book fetchAndSave(String isbn){
        log.info("DB에 없는 책 - 알라딘 API 호출 및 저장 isbn: {}", isbn);

        AladinApiResponse response = aladinApiClient.searchBooks(isbn);

        if(response.getItem() == null || response.getItem().isEmpty()){
            throw new IllegalArgumentException("해당 ISBN의 책을 찾을 수 없습니다: "+isbn);
        }

        AladinBookItem item = response.getItem().getFirst();
        Book book = aladinBookToBook(item);

        return bookRepository.save(book);
    }

    private Book aladinBookToBook(AladinBookItem item){
        LocalDate publishDate = null;

        // 오래된 책이나 몇몇 책은 출간일 정보가 없는 경우가 있기 때문에 해당 경우 예방책
        if (item.getPubDate() != null && !item.getPubDate().isEmpty()){
            publishDate = LocalDate.parse(item.getPubDate(), DateTimeFormatter.ISO_DATE);
        }

        return Book.builder()
                .isbn(item.getIsbn13())
                .title(item.getTitle())
                .author(item.getAuthor())
                .publisher(item.getPublisher())
                .coverImage(item.getCover())
                .publishDate(publishDate)
                .build();
    }

}
