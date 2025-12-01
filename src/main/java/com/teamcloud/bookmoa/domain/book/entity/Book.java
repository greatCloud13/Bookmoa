package com.teamcloud.bookmoa.domain.book.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "books")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

}
