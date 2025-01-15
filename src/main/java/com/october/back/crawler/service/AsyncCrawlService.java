package com.october.back.crawler.service;

import com.october.back.global.common.ErrorCode;
import com.october.back.global.exception.CrawlException;
import com.october.back.photobooth.entity.BrandType;
import com.october.back.photobooth.entity.PhotoBooth;
import lombok.RequiredArgsConstructor;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
@RequiredArgsConstructor
@Service
public class AsyncCrawlService {
    private static final String LIFE_FOUR_CUT_BASE_URL = "https://lifefourcuts.com/Store01/?sort=TIME&keyword_type=all&page=";
    private static final String DONT_LOOKUP_BASE_URL = "https://dontlxxkup.kr/store/?sort=TIME&keyword_type=all&page=";
    private static final String HARU_FILM_BASE_URL = "http://harufilm.com/";
    private static final String PHOTOISM_BASE_URL = "https://photoism.co.kr/%d/?sort=TIME&keyword_type=all&page=%d";

    private String getRandomUserAgent() {
        String[] userAgents = {
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36",
                "Mozilla/5.0 (Linux; Android 10) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Mobile Safari/537.36"
        };
        return userAgents[(int) (Math.random() * userAgents.length)];
    }

    private Connection createConnectionWithProxy(String url) {
        return Jsoup.connect(url)
                .userAgent(getRandomUserAgent())
                .timeout(10000)
                .header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                .header("accept-language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7")
                .header("cache-control", "no-cache");
    }
    @Async
    public CompletableFuture<List<PhotoBooth>> lifeFourCutAsync(int pageNumber, Set<PhotoBooth> existingPhotoBooths){
        List<PhotoBooth> newOrUpdatedPhotoBooths = new ArrayList<>();
        String url = LIFE_FOUR_CUT_BASE_URL + pageNumber;
        Connection conn = createConnectionWithProxy(url); // 프록시 연결 사용

        try {
            Document document = conn.get();
            Elements titles = document.select("div.map_contents.inline-blocked");

            for (Element e : titles) {
                String placeName = "인생네컷 " + e.select("div.tit").text().trim();
                String address = e.select("p.adress").text().trim();

                // 지점명으로 중복 검사
                synchronized (existingPhotoBooths) {
                    Optional<PhotoBooth> existingBoothOpt = existingPhotoBooths.stream()
                            .filter(booth -> booth.getPlaceName().equals(placeName))
                            .findFirst();

                    if (existingBoothOpt.isPresent()) {
                        // 중복된 경우 업데이트
                        PhotoBooth existingBooth = existingBoothOpt.get();
                        existingBooth.updateFields(BrandType.인생네컷, placeName, address);
                        newOrUpdatedPhotoBooths.add(existingBooth);
                    } else {
                        // 새 데이터를 추가
                        PhotoBooth newBooth = PhotoBooth.builder()
                                .placeName(placeName)
                                .address(address)
                                .brandName(BrandType.인생네컷)
                                .build();

                        existingPhotoBooths.add(newBooth); // 중복 방지를 위해 추가
                        newOrUpdatedPhotoBooths.add(newBooth); // 저장할 리스트에도 추가
                    }
                }
            }
        } catch (IOException e) {
            throw new CrawlException(e.getMessage(), ErrorCode.CRAWL_CONNECTION_FAILED);
        }

        return CompletableFuture.completedFuture(newOrUpdatedPhotoBooths);
    }

    @Async
    public CompletableFuture<List<PhotoBooth>> haruFilmCutAsync(int code, Set<PhotoBooth> existingPhotoBooths) {
        List<PhotoBooth> newOrUpdatedPhotoBooths = new ArrayList<>();
        String url = HARU_FILM_BASE_URL + code;
        Connection conn = createConnectionWithProxy(url); // 프록시 연결 사용

        try {
            Document document = conn.get();
            Elements titles = document.select("p.title");

            for (Element e : titles) {
                String address = e.select("span.body").text().trim();
                String placeName = "하루필름 " + e.text().replace(address, "").trim();
                synchronized (existingPhotoBooths) {
                    Optional<PhotoBooth> existingBoothOpt = existingPhotoBooths.stream()
                            .filter(booth -> booth.getPlaceName().equals(placeName))
                            .findFirst();

                    if (existingBoothOpt.isPresent()) {
                        // 중복된 경우 업데이트
                        PhotoBooth existingBooth = existingBoothOpt.get();
                        existingBooth.updateFields(BrandType.하루필름, placeName, address);
                        newOrUpdatedPhotoBooths.add(existingBooth);
                    } else {
                        // 새 데이터를 추가
                        PhotoBooth newBooth = PhotoBooth.builder()
                                .placeName(placeName)
                                .address(address)
                                .brandName(BrandType.하루필름)
                                .build();

                        existingPhotoBooths.add(newBooth); // 중복 방지를 위해 추가
                        newOrUpdatedPhotoBooths.add(newBooth); // 저장할 리스트에도 추가
                    }
                }
            }
        } catch (IOException e) {
            throw new CrawlException(e.getMessage(), ErrorCode.CRAWL_CONNECTION_FAILED);
        }

        return CompletableFuture.completedFuture(newOrUpdatedPhotoBooths);
    }

    @Async
    public CompletableFuture<List<PhotoBooth>> dontLookupAsync(int pageNumber, Set<PhotoBooth> existingPhotoBooths){
        List<PhotoBooth> newOrUpdatedPhotoBooths = new ArrayList<>();
        String url = DONT_LOOKUP_BASE_URL + pageNumber;
        Connection conn = createConnectionWithProxy(url); // 프록시 연결 사용

        try {
            Document document = conn.get();
            Elements titles = document.select("div.map_contents.inline-blocked");

            for (Element e : titles) {
                String placeName = e.select("div.tit").text().trim();
                String address = e.select("p.adress").text().trim();

                synchronized (existingPhotoBooths) {
                    Optional<PhotoBooth> existingBoothOpt = existingPhotoBooths.stream()
                            .filter(booth -> booth.getPlaceName().equals(placeName))
                            .findFirst();

                    if (existingBoothOpt.isPresent()) {
                        // 중복된 경우 업데이트
                        PhotoBooth existingBooth = existingBoothOpt.get();
                        existingBooth.updateFields(BrandType.돈룩업, placeName, address);
                        newOrUpdatedPhotoBooths.add(existingBooth);
                    } else {
                        // 새 데이터를 추가
                        PhotoBooth newBooth = PhotoBooth.builder()
                                .placeName(placeName)
                                .address(address)
                                .brandName(BrandType.돈룩업)
                                .build();

                        existingPhotoBooths.add(newBooth); // 중복 방지를 위해 추가
                        newOrUpdatedPhotoBooths.add(newBooth); // 저장할 리스트에도 추가
                    }
                }
            }
        } catch (IOException e) {
            throw new CrawlException(e.getMessage(), ErrorCode.CRAWL_CONNECTION_FAILED);
        }

        return CompletableFuture.completedFuture(newOrUpdatedPhotoBooths);
    }

    @Async
    public CompletableFuture<List<PhotoBooth>> photoismAsync(int brandId, int pageNumber, Set<PhotoBooth> existingPhotoBooths){
        List<PhotoBooth> newOrUpdatedPhotoBooths = new ArrayList<>();
        String url = String.format(PHOTOISM_BASE_URL, brandId, pageNumber);
        Connection conn = createConnectionWithProxy(url); // 프록시 연결 사용


        try {
            Document document = conn.get();
            Elements titles = document.select("div.map_container.clearfix.map-inner._map_container");

            for (Element e : titles) {
                String placeName = e.select("div.tit").text()
                        .replace("포토이즘 박스", "포토이즘박스")
                        .replace("포토이즘 컬러드", "포토이즘컬러드").trim();
                String address = e.select("p.adress").text().trim();

                synchronized (existingPhotoBooths) {
                    Optional<PhotoBooth> existingBoothOpt = existingPhotoBooths.stream()
                            .filter(booth -> booth.getPlaceName().equals(placeName))
                            .findFirst();

                    if (existingBoothOpt.isPresent()) {
                        // 중복된 경우 업데이트
                        PhotoBooth existingBooth = existingBoothOpt.get();
                        existingBooth.updateFields(BrandType.포토이즘, placeName, address);
                        newOrUpdatedPhotoBooths.add(existingBooth);
                    } else {
                        // 새 데이터를 추가
                        PhotoBooth newBooth = PhotoBooth.builder()
                                .placeName(placeName)
                                .address(address)
                                .brandName(BrandType.포토이즘)
                                .build();

                        existingPhotoBooths.add(newBooth); // 중복 방지를 위해 추가
                        newOrUpdatedPhotoBooths.add(newBooth); // 저장할 리스트에도 추가
                    }
                }
            }
        } catch (IOException e) {
            throw new CrawlException(e.getMessage(), ErrorCode.CRAWL_CONNECTION_FAILED);
        }

        return CompletableFuture.completedFuture(newOrUpdatedPhotoBooths);
    }
}
