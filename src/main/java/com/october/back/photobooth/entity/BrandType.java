package com.october.back.photobooth.entity;

import java.util.Arrays;

public enum BrandType {
    인생네컷,
    포토이즘,
    하루필름,
    돈룩업;

    public static BrandType fromKoreanName(String koreanName) {
        return Arrays.stream(values())
                .filter(brand -> brand.name().equals(koreanName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid brand name: " + koreanName));
    }
}
