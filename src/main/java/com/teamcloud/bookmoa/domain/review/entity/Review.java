package com.teamcloud.bookmoa.domain.review.entity;

import com.teamcloud.bookmoa.domain.book.entity.Book;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@Getter
@NoArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "isbn", nullable = false)
    private Book book;

    @Column(nullable = false)
    private String reviewer;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Integer rating;

    private LocalDateTime createdAt;

    @Builder
    public Review(Book book, String reviewer, String content, Integer rating) {
        this.book = book;
        this.reviewer = reviewer;
        this.content = content;
        this.rating = rating;
        this.createdAt = LocalDateTime.now();
    }

    // 리뷰 수정
    public void update(String content, Integer rating){
        this.content = content;
        this.rating = rating;
    }

}
