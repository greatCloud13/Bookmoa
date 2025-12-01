package com.teamcloud.bookmoa.domain.book.client;

import com.teamcloud.bookmoa.domain.book.dto.AladinApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
public class AladinApiClient {

    private final RestClient restClient;
    private final String apiKey;

    public AladinApiClient(
            @Value("${aladin.api.key}") String apiKey,
            @Value("${aladin.api.base-url}") String baseUrl
    ){
        this.apiKey = apiKey;
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public AladinApiResponse searchBooks(String query){
        log.info("알라딘 API 호출 - 검색어: {}", query);

        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/ItemSearch.aspx")
                        .queryParam("ttbkey", apiKey)
                        .queryParam("Query", query)
                        .queryParam("output", "js")
                        .queryParam("Version", "20131101")
                        .build())
                .retrieve()
                .body(AladinApiResponse.class);
    }
}
