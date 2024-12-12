package com.october.back.photobooth.entity;

import com.october.back.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
    @Column(name = "brandName", nullable = false)
    private String brandName;

    @Builder
    private PhotoBooth(double latitude, double longitude, String brandName) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.brandName = brandName;
    }
}
