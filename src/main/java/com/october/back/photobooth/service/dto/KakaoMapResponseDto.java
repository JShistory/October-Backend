package com.october.back.photobooth.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class KakaoMapResponseDto {

    private Meta meta;
    private List<Document> documents;

    @Setter
    @Getter
    public static class Meta {
        private int total_count;        // 검색된 문서 수
        private int pageable_count;     // 페이지 요청 가능 문서 수
        private boolean is_end;         // 마지막 페이지 여부

        @Override
        public String toString() {
            return "Meta{" +
                    "total_count=" + total_count +
                    ", pageable_count=" + pageable_count +
                    ", is_end=" + is_end +
                    '}';
        }
    }

    @Setter
    @Getter
    public static class Document {
        private String address_name;        // 전체 지번 주소
        private String category_group_code; // 장소 그룹 코드
        private String category_group_name; // 장소 그룹명
        private String category_name;       // 장소 상세 카테고리명
        private String distance;            // 중심 좌표까지의 거리 (미터)
        private String id;                  // 장소 ID
        private String phone;               // 전화번호
        private String place_name;          // 장소명
        private String place_url;           // 장소 상세페이지 URL
        private String road_address_name;   // 전체 도로명 주소
        private String x;                   // X 좌표값 혹은 경도(longitude)
        private String y;                   // Y 좌표값 혹은 위도(latitude)

        @Override
        public String toString() {
            return "Document{" +
                    "address_name='" + address_name + '\'' +
                    ", category_group_code='" + category_group_code + '\'' +
                    ", category_group_name='" + category_group_name + '\'' +
                    ", category_name='" + category_name + '\'' +
                    ", distance='" + distance + '\'' +
                    ", id='" + id + '\'' +
                    ", phone='" + phone + '\'' +
                    ", place_name='" + place_name + '\'' +
                    ", place_url='" + place_url + '\'' +
                    ", road_address_name='" + road_address_name + '\'' +
                    ", x='" + x + '\'' +
                    ", y='" + y + '\'' +
                    '}';
        }
    }
}
