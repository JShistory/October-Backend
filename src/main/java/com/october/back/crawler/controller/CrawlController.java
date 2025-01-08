package com.october.back.crawler.controller;

import com.october.back.crawler.service.DontLookUpService;
import com.october.back.crawler.service.HaruFilmService;
import com.october.back.crawler.service.LifeFourCutService;
import com.october.back.crawler.service.PhotoismService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RequestMapping("/api/v1/crawl")
@RequiredArgsConstructor
@RestController
public class CrawlController {
    private final LifeFourCutService lifeFourCutService;
    private final HaruFilmService haruFilmService;
    private final DontLookUpService dontLookUpService;
    private final PhotoismService photoismService;

    @GetMapping("/harufilm")
    public ResponseEntity<String> crawlHaruFilm(){
        haruFilmService.crawl();
        return ResponseEntity.ok("HaruFilm crawled Successfully");
    }

    @GetMapping("/photoism")
    public ResponseEntity<String> crawlPhotoism(){
        photoismService.crawl();
        return ResponseEntity.ok("Photoism crawled Successfully");
    }

    @GetMapping("/lifefourcut")
    public ResponseEntity<String> crawlLifeFourCut(){
        lifeFourCutService.crawl();
        return ResponseEntity.ok("LifeFourCut crawled Successfully");
    }

    @GetMapping("/dontlookup")
    public ResponseEntity<String> crawlDontLookUp(){
        dontLookUpService.crawl();
        return ResponseEntity.ok("DontLookUp crawled Successfully");
    }
}
