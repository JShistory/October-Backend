package com.october.back.photobooth.service;

import com.october.back.photobooth.entity.BrandType;
import com.october.back.photobooth.entity.PhotoBooth;
import com.october.back.photobooth.repository.PhotoBoothRepository;
import com.october.back.photobooth.service.dto.KakaoMapResponseDto;
import com.october.back.photobooth.service.dto.KakaoMapResponseDto.Document;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;

import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class KakaoMapService {
    private static final Logger logger = LoggerFactory.getLogger(KakaoMapService.class);

    private final WebClient kakaoClient;
    private final PhotoBoothRepository photoBoothRepository;

    // WebClient 커스터마이징 - 헤더 로깅용 필터 추가
    private WebClient customizeClient(WebClient client) {
        return client.mutate()
                .filter(logRequest())
                .build();
    }

    private ExchangeFilterFunction logRequest() {
        return (request, next) -> {
            logger.info("Request: {} {}", request.method(), request.url());
            request.headers().forEach((name, values) ->
                    values.forEach(value -> logger.info("Header '{}' = '{}'", name, value)));
            return next.exchange(request);
        };
    }

    public Mono<KakaoMapResponseDto> searchByKeyword(String keyword, String x, String y, int radius, int page) {
        WebClient customClient = customizeClient(kakaoClient); // 커스터마이징한 WebClient 사용
        return customClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v2/local/search/keyword.json")
                        .queryParam("query", keyword)
                        .queryParam("x", x)
                        .queryParam("y", y)
                        .queryParam("radius", radius)
                        .queryParam("page", page)
                        .build())
                .retrieve()
                .bodyToMono(KakaoMapResponseDto.class)
                .doOnSubscribe(subscription -> logger.info("Making request for keyword: {}, page: {}", keyword, page))
                .doOnTerminate(() -> logger.info("Request completed for keyword: {}, page: {}", keyword, page));
    }

    public void searchAndSaveAll(String keyword, String x, String y) {
        int radius = 20000; // 20km 반경
        int page = 1; // 첫 페이지부터 시작

        // 재귀적으로 모든 페이지를 검색
        searchAndSaveRecursive(keyword, x, y, radius, page);
    }

    private void searchAndSaveRecursive(String keyword, String x, String y, int radius, int page) {
        logger.info("Searching for keyword: {}, page: {}", keyword, page);  // 로그 출력
        searchByKeyword(keyword, x, y, radius, page)
                .doOnNext(response -> {
                    logger.info("Response Meta received: {}", response.getMeta());  // 로그 출력
                    logger.info("Response Document received: {}", response.getDocuments());  // 로그 출력

                    List<Document> documents = response.getDocuments();
                    documents.forEach(doc -> {
                        PhotoBooth photoBooth = PhotoBooth.builder()
                                .placeName(doc.getPlace_name())
                                .address(doc.getAddress_name())
                                .phone(doc.getPhone())
                                .placeUrl(doc.getPlace_url())
                                .latitude(Double.parseDouble(doc.getX()))
                                .longitude(Double.parseDouble(doc.getY()))
                                .brandName(BrandType.fromKoreanName(keyword))
                                .build();
                        photoBoothRepository.save(photoBooth); // 데이터베이스에 저장
                    });

                    if (!response.getMeta().is_end()) {
                        searchAndSaveRecursive(keyword, x, y, radius, page + 1);
                    }
                })
                .subscribe();
    }
}
