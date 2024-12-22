package com.october.back.photobooth.entity;

import com.october.back.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class PhotoBooth extends BaseEntity {

    @Column(name = "latitude", nullable = false)
    private double latitude;

    @Column(name = "longitude", nullable = false)
    private double longitude;

    @Enumerated(EnumType.STRING)
    @Column(name = "brand_name", length = 50, nullable = false)
    private BrandType brandName;

    @Column(name = "place_name", nullable = false)
    private String placeName;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "road_address", nullable = true)
    private String roadAddress;

    @Column(name = "phone", nullable = true)
    private String phone;

    @Column(name = "place_url", nullable = true, columnDefinition = "TEXT")
    private String placeUrl;

    @Column(name = "distance", nullable = true)
    private int distance; // 거리 (예: 사용자가 검색 기준으로부터의 거리)

    @Builder
    private PhotoBooth(double latitude, double longitude, BrandType brandName, String placeName,
                       String address, String roadAddress, String phone, String placeUrl, int distance) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.brandName = brandName;
        this.placeName = placeName;
        this.address = address;
        this.roadAddress = roadAddress;
        this.phone = phone;
        this.placeUrl = placeUrl;
        this.distance = distance;
    }
}
