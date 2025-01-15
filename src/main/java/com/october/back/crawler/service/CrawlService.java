package com.october.back.crawler.service;

import com.october.back.photobooth.entity.BrandType;
import com.october.back.photobooth.entity.PhotoBooth;
import com.october.back.photobooth.repository.PhotoBoothRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CrawlService {
    private final PhotoBoothRepository photoBoothRepository;
    private final AsyncCrawlService asyncCrawlService;
    @Transactional
    public void lifeFourCut() {
        Set<PhotoBooth> existingPhotoBooths = new HashSet<>(
                photoBoothRepository.findByBrandName(BrandType.인생네컷)
                        .stream()
                        .collect(Collectors.toSet())
        );

        int totalPages = 49;
        List<CompletableFuture<List<PhotoBooth>>> futures = new ArrayList<>();

        for (int i = 1; i <= totalPages; i++) {
            int pageNumber = i;
            futures.add(asyncCrawlService.lifeFourCutAsync(pageNumber, existingPhotoBooths));
        }

        // 비동기 작업 결과 모아서 저장
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> futures.stream()
                        .map(CompletableFuture::join)
                        .flatMap(List::stream)
                        .collect(Collectors.toList()))
                .thenAccept(photoBoothRepository::saveAll)
                .join(); // 비동기 작업 완료 대기
    }

    @Transactional
    public void dontLookUp() {
        Set<PhotoBooth> existingPhotoBooths = new HashSet<>(
                photoBoothRepository.findByBrandName(BrandType.돈룩업)
                        .stream()
                        .collect(Collectors.toSet())
        );

        int totalPages = 7;
        List<CompletableFuture<List<PhotoBooth>>> futures = new ArrayList<>();

        for (int i = 1; i <= totalPages; i++) {
            int pageNumber = i;
            futures.add(asyncCrawlService.dontLookupAsync(pageNumber, existingPhotoBooths));
        }

        // 비동기 작업 결과 모아서 저장
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> futures.stream()
                        .map(CompletableFuture::join)
                        .flatMap(List::stream)
                        .collect(Collectors.toList()))
                .thenAccept(photoBoothRepository::saveAll)
                .join(); // 비동기 작업 완료 대기
    }

    @Transactional
    public void haruFilm() {
        Set<PhotoBooth> existingPhotoBooths = new HashSet<>(
                photoBoothRepository.findByBrandName(BrandType.하루필름)
                        .stream()
                        .collect(Collectors.toSet())
        );
        int[] codes = {202, 203, 204, 205, 206, 207, 208, 209};
        List<CompletableFuture<List<PhotoBooth>>> futures = new ArrayList<>();

        for (int code : codes) {
            futures.add(asyncCrawlService.haruFilmCutAsync(code, existingPhotoBooths));
        }

        // 비동기 작업 결과 모아서 저장
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> futures.stream()
                        .map(CompletableFuture::join)
                        .flatMap(List::stream)
                        .collect(Collectors.toList()))
                .thenAccept(photoBoothRepository::saveAll)
                .join(); // 비동기 작업 완료 대기
    }

    @Transactional
    public void photoismBox() {
        int[] infos = new int[]{279, 45}; //포토이즘 박스 (url, page)

        Set<PhotoBooth> existingPhotoBooths = new HashSet<>(
                photoBoothRepository.findByBrandName(BrandType.포토이즘)
                        .stream()
                        .collect(Collectors.toSet())
        );

        List<CompletableFuture<List<PhotoBooth>>> futures = new ArrayList<>();

        for (int i = 1; i <= infos[1]; i++) {
            futures.add(asyncCrawlService.photoismAsync(infos[0] ,i, existingPhotoBooths));
        }

        // 비동기 작업 결과 모아서 저장
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> futures.stream()
                        .map(CompletableFuture::join)
                        .flatMap(List::stream)
                        .collect(Collectors.toList()))
                .thenAccept(photoBoothRepository::saveAll)
                .join(); // 비동기 작업 완료 대기
    }

    @Transactional
    public void photoismColor() {
        int[] infos = new int[]{280, 9}; //포토이즘 컬러드 (url, page)

        Set<PhotoBooth> existingPhotoBooths = new HashSet<>(
                photoBoothRepository.findByBrandName(BrandType.포토이즘)
                        .stream()
                        .collect(Collectors.toSet())
        );

        List<CompletableFuture<List<PhotoBooth>>> futures = new ArrayList<>();

        for (int i = 1; i <= infos[1]; i++) {
            futures.add(asyncCrawlService.photoismAsync(infos[0] ,i, existingPhotoBooths));
        }

        // 비동기 작업 결과 모아서 저장
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> futures.stream()
                        .map(CompletableFuture::join)
                        .flatMap(List::stream)
                        .collect(Collectors.toList()))
                .thenAccept(photoBoothRepository::saveAll)
                .join(); // 비동기 작업 완료 대기
    }

}
