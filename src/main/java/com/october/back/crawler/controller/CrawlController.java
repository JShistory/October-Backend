package com.october.back.crawler.controller;

import com.october.back.crawler.service.CrawlService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RequestMapping("/security/crawl")
@RequiredArgsConstructor
@RestController
public class CrawlController {
    private final CrawlService crawlService;
    @GetMapping("/all")
    public ResponseEntity<String> crawlAll() {
        crawlService.photoismColor();
        crawlService.photoismBox();
        crawlService.lifeFourCut();
        crawlService.haruFilm();
        crawlService.dontLookUp();
        return ResponseEntity.ok("All PhotoBooth crawled Successfully");
    }
    @GetMapping("/harufilm")
    public ResponseEntity<String> crawlHaruFilm(){
        crawlService.haruFilm();
        return ResponseEntity.ok("HaruFilm crawled Successfully");
    }

    @GetMapping("/photoismbox")
    public ResponseEntity<String> crawlPhotoismBox(){
        crawlService.photoismBox();
        return ResponseEntity.ok("PhotoismBox crawled Successfully");
    }

    @GetMapping("/photoismcolor")
    public ResponseEntity<String> crawlPhotoismColor(){
        crawlService.photoismColor();
        return ResponseEntity.ok("PhotoismColor crawled Successfully");
    }

    @GetMapping("/lifefourcut")
    public ResponseEntity<String> crawlLifeFourCut(){
        crawlService.lifeFourCut();
        return ResponseEntity.ok("LifeFourCut crawled Successfully");
    }

    @GetMapping("/dontlookup")
    public ResponseEntity<String> crawlDontLookUp(){
        crawlService.dontLookUp();
        return ResponseEntity.ok("DontLookUp crawled Successfully");
    }
}
