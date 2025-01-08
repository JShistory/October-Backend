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
public class HaruFilmService implements CrawlService {
    private final PhotoBoothRepository photoBoothRepository;
    private static final String HARU_FILM_BASE_URL = "http://harufilm.com/";

    @Transactional
    public void crawl() {
        List<PhotoBooth> savePhotoBooth = new ArrayList<>();
        Set<String> existingPlaceNames = new HashSet<>(
                photoBoothRepository.findByBrandName(BrandType.하루필름)
                        .stream()
                        .map(PhotoBooth::getPlaceName)
                        .collect(Collectors.toSet())
        );

        int[] codes = {202, 203, 204, 205, 206, 207, 208, 209};

        for (int code : codes) {
            String url = HARU_FILM_BASE_URL + code;
            Connection conn = Jsoup.connect(url);

            try {
                Document document = conn.get();
                Elements titles = document.select("p.title");

                for (Element e : titles) {
                    String address = e.select("span.body").text().trim();
                    String placeName = "하루필름 " + e.text().replace(address, "").trim();

                    if (existingPlaceNames.contains(placeName)) {
                        continue;
                    }
                    PhotoBooth photoBooth = PhotoBooth.builder()
                            .placeName(placeName)
                            .address(address)
                            .brandName(BrandType.하루필름)
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