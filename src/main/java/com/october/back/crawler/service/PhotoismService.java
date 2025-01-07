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

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class PhotoismService implements CrwalService {
    private final PhotoBoothRepository photoBoothRepository;
    private static final String PHOTOISM_BASE_URL = "https://photoism.co.kr/%d/?sort=TIME&keyword_type=all&page=%d";

    @Transactional
    public void crawl() {
        int[][] infos = {
                {279, 50}, // 포토이즘 박스 (url, page)
                {280, 20}, // 포토이즘 컬러드 (url, page)
        };
        List<PhotoBooth> savePhotoBooth = new ArrayList<>();
        Set<String> existingPlaceNames = new HashSet<>(
                photoBoothRepository.findByBrandName(BrandType.인생네컷)
                        .stream()
                        .map(PhotoBooth::getPlaceName)
                        .collect(Collectors.toSet())
        );
        for (int[] info : infos) {
            for (int i = 1; i <= info[1]; i++) {
                String url = String.format(PHOTOISM_BASE_URL, info[0], i);
                Connection conn = Jsoup.connect(url);

                try {
                    Document document = conn.get();
                    Elements titles = document.select("div.map_container.clearfix.map-inner._map_container");

                    for (Element e : titles) {
                        String placeName = e.select("div.tit").text()
                                .replace("포토이즘 박스", "포토이즘박스")
                                .replace("포토이즘 컬러드", "포토이즘컬러드").trim();
                        String address = e.select("p.adress").text().trim();
                        if (existingPlaceNames.contains(placeName)) {
                            continue;
                        }
                        PhotoBooth photoBooth = PhotoBooth.builder()
                                .placeName(placeName)
                                .address(address)
                                .brandName(BrandType.포토이즘)
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
}
