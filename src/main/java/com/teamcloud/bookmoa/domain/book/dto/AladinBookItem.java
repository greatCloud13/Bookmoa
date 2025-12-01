package com.teamcloud.bookmoa.domain.book.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AladinBookItem {

    private String title;

    private String author;

    @JsonProperty("pubDate")
    private String pubDate;

    private String description;

    private String isbn;

    private String isbn13;

    private String cover;

    private String publisher;
}
