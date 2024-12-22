package com.october.back.photobooth.service;

import com.october.back.photobooth.entity.BrandType;
import com.october.back.photobooth.entity.PhotoBooth;
import com.october.back.photobooth.repository.PhotoBoothRepository;
import com.october.back.photobooth.service.dto.KakaoMapResponseDto;
import com.october.back.photobooth.service.dto.KakaoMapResponseDto.Document;
import com.october.back.region.entity.Region;
import com.october.back.region.service.RegionService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class KakaoMapService {
    private static final Logger logger = LoggerFactory.getLogger(KakaoMapService.class);

    private final WebClient kakaoClient;
    private final PhotoBoothRepository photoBoothRepository;
    private final RegionService regionService;

    public Mono<KakaoMapResponseDto> searchByKeyword(String keyword, String x, String y, int radius, int page) {
        return kakaoClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v2/local/search/keyword.json")
                        .queryParam("query", keyword)
                        .queryParam("x", x)
                        .queryParam("y", y)
                        .queryParam("radius", radius)
                        .queryParam("page", page)
                        .build())
                .retrieve()
                .bodyToMono(KakaoMapResponseDto.class);
    }

    public void searchAndSaveAll(String keyword) {
        List<Region> regions = regionService.findAll();

        // 각 지역에 대해 검색하고 저장
        for (Region region : regions) {
            String x = region.getLongitude();
            String y = region.getLatitude();
            int radius = 20000;
            int page = 1;
            searchAndSaveRecursive(keyword, x, y, radius, page); // 지역에 대해 검색
        }
    }

    private void searchAndSaveRecursive(String keyword, String x, String y, int radius, int page) {
        logger.info("Searching for keyword: {}, page: {}", keyword, page);

        searchByKeyword(keyword, x, y, radius, page)
                .doOnNext(response -> {
                    logger.info("Response Meta received: {}", response.getMeta());
                    logger.info("Response Document received: {}", response.getDocuments());

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
                        photoBoothRepository.save(photoBooth);
                        logger.info("Saved PhotoBooth: {}", photoBooth);
                    });

                    if (!response.getMeta().is_end()) {
                        searchAndSaveRecursive(keyword, x, y, radius, page + 1);
                    }
                })
                .subscribe();
    }
}
