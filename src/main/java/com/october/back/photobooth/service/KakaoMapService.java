package com.october.back.photobooth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class KakaoMapService {
    private final WebClient kakaoClient;

    public Mono<String> searchByKeyword(String keyword, double x, double y, int radius) {
        return kakaoClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v2/local/search/keyword.json")
                        .queryParam("query", keyword)
                        .queryParam("x", x)
                        .queryParam("y", y)
                        .queryParam("radius", radius)
                        .build())
                .retrieve()
                .bodyToMono(String.class); // 필요시 DTO로 매핑 가능
    }
}
