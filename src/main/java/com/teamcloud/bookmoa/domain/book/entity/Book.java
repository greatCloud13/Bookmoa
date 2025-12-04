package com.teamcloud.bookmoa.domain.book.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "books")
@Getter
@NoArgsConstructor
public class Book implements Serializable {

    @Id
    @Column(length = 13)
    private String isbn;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String publisher;

    private String coverImage;

    private LocalDate publishDate;

    @Column
    private Double averageRating; // 평균 평점

    @Column
    private Integer reviewCount; // 리뷰 수

    @Builder
    public Book(String isbn, String title, String author, String publisher, String coverImage, LocalDate publishDate){
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.coverImage = coverImage;
        this.publishDate = publishDate;
        this.averageRating = 0.0; // 초기값
        this.reviewCount = 0;       // 초기값
    }

    // 통계 업데이트 메서드
    public void updateStatistics(Double averageRating, Integer reviewCount){
        this.averageRating = averageRating;
        this.reviewCount = reviewCount;
    }

}
