package com.october.back.crawler.service;

import com.october.back.photobooth.entity.BrandType;
import com.october.back.photobooth.entity.PhotoBooth;
import com.october.back.photobooth.repository.PhotoBoothRepository;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class LifeFourCutService implements CrwalService {
    private final PhotoBoothRepository photoBoothRepository;
    private static final String LIFE_FOUR_CUT_BASE_URL = "https://lifefourcuts.com/Store01/?sort=TIME&keyword_type=all&page=";
    @Transactional
    public void crawl() {
        List<PhotoBooth> savePhotoBooth = new ArrayList<>();
        List<String> findAllPlaceNameList = photoBoothRepository.findAll()
                .stream()
                .map(PhotoBooth -> PhotoBooth.getPlaceName())
                .collect(Collectors.toList());
        int page = 49;
        for (int i = 1; i <= page; i++) {
            String url = LIFE_FOUR_CUT_BASE_URL + i;
            try {
                Document document = Jsoup.connect(url).get();
                Elements titles = document.select("div.map_contents.inline-blocked");

                for (Element e : titles) {
                    String placeName = "인생네컷 " + e.select("div.tit").text().trim();
                    String address = e.select("p.adress").text().trim();
                    String phone = e.select("p.tell > a.blocked").attr("href").replace("tel:", "").trim(); // 전화번호
                    // 지점명으로 중복 검사
                    if (photoBoothRepository.existsByPlaceName(placeName)) continue;
                    PhotoBooth photoBooth = PhotoBooth.builder()
                            .placeName(placeName)
                            .address(address)
                            .phone(phone)
                            .brandName(BrandType.인생네컷)
                            .build();
                    savePhotoBooth.add(photoBooth);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        photoBoothRepository.saveAll(savePhotoBooth);

    }
}