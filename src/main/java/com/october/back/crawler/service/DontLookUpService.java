package com.october.back.crawler.service;

import com.october.back.photobooth.entity.BrandType;
import com.october.back.photobooth.entity.PhotoBooth;
import com.october.back.photobooth.repository.PhotoBoothRepository;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class DontLookUpService implements CrawlService {
    private final PhotoBoothRepository photoBoothRepository;
    private static final String HARU_FILM_BASE_URL = "https://dontlxxkup.kr/store/?sort=TIME&keyword_type=all&page=";

    @Transactional
    public void crawl() {
        List<PhotoBooth> savePhotoBooth = new ArrayList<>();
        Set<String> existingPlaceNames = new HashSet<>(
                photoBoothRepository.findByBrandName(BrandType.돈룩업)
                        .stream()
                        .map(PhotoBooth::getPlaceName)
                        .collect(Collectors.toSet())
        );

        int page = 10;
        for (int i=1; i<=page; i++) {
            String url = HARU_FILM_BASE_URL + i;
            Connection conn = Jsoup.connect(url);

            try {
                Document document = conn.get();
                Elements titles = document.select("div.map_contents.inline-blocked");

                for (Element e : titles) {
                    String placeName = e.select("div.tit").text().trim();
                    String address = e.select("p.adress").text().trim();
                    String phone = e.select("p.tell > a.blocked").attr("href").replace("tel:", "").trim(); // 전화번호

                    if (existingPlaceNames.contains(placeName)) {
                        continue;
                    }
                    PhotoBooth photoBooth = PhotoBooth.builder()
                            .placeName(placeName)
                            .address(address)
                            .brandName(BrandType.돈룩업)
                            .phone(phone)
                            .build();

                    savePhotoBooth.add(photoBooth);
                    existingPlaceNames.add(placeName);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        photoBoothRepository.saveAll(savePhotoBooth);
    }
}