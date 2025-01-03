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

    @Enumerated(EnumType.STRING)
    @Column(name = "brand_name", length = 50, nullable = false)
    private BrandType brandName;

    @Column(name = "place_name", nullable = false)
    private String placeName;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "phone", nullable = true)
    private String phone;

    @Builder
    private PhotoBooth(BrandType brandName, String placeName,
                       String address, String phone) {
        this.brandName = brandName;
        this.placeName = placeName;
        this.address = address;
        this.phone = phone;
    }

    public PhotoBooth updateFields(BrandType brandName, String placeName,
                                   String address, String phone) {
        this.brandName = brandName != null ? brandName : this.brandName;
        this.placeName = placeName != null ? placeName : this.placeName;
        this.address = address != null ? address : this.address;
        this.phone = phone != null ? phone : this.phone;
        return this;
    }

}
