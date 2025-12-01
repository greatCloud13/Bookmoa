package com.teamcloud.bookmoa.domain.book.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class AladinApiResponse {

    private int totalResults;

    private int startIndex;

    private int itemPerPage;

    private String query;

    private List<AladinBookItem> item;
}
