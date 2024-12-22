package com.october.back.photobooth.controller;

import com.october.back.photobooth.service.KakaoMapService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class PhotoBoothController {
    private final KakaoMapService kakaoMapService;

    @GetMapping("/kakao")
    public String getPhotoBoothKeyword(@RequestParam String keyword) {

        kakaoMapService.searchAndSaveAll(keyword);
        return "kakao Map 호출 서비스입니다.";
    }
}
