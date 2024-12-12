package com.october.back.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${kakao.api.base-url}")
    private String kakaoApiBaseUrl;

    @Value("${kakao.api.rest-api-key}")
    private String kakaoRestApiKey;

    @Bean
    public WebClient kakaoClient(WebClient.Builder builder) {
        return builder
                .baseUrl(kakaoApiBaseUrl) // Kakao API 기본 URL 설정
                .defaultHeader("Authorization", "KakaoAK " + kakaoRestApiKey) // Kakao API 인증 헤더
                .build();
    }


}

