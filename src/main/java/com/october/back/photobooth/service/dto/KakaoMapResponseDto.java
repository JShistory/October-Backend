package com.october.back.photobooth.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class KakaoMapResponseDto {

    private List<Document> documents;

    @Setter
    @Getter
    public static class Document {
        private String address_name;
        private String category_group_code;
        private String category_group_name;
        private String category_name;
        private String distance;
        private String id;
        private String phone;
        private String place_name;
        private String place_url;
        private String road_address_name;
        private String x;
        private String y;
    }
}