package com.october.back.region.entity;

import com.october.back.global.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "REGION")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Region extends BaseEntity {

    @Column(name = "province", nullable = false)
    private String province;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "longitude", nullable = false)
    private String longitude;

    @Column(name = "latitude", nullable = false)
    private String latitude;

    @Builder
    private Region(String province, String city, String longitude, String latitude) {
        this.province = province;
        this.city = city;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return "Region{" +
                "province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                '}';
    }
}
