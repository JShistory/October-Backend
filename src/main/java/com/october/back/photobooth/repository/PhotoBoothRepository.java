package com.october.back.photobooth.repository;

import com.october.back.photobooth.entity.BrandType;
import com.october.back.photobooth.entity.PhotoBooth;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoBoothRepository extends JpaRepository<PhotoBooth, Long> {
    PhotoBooth findByPlaceNameAndAddress(String placeName, String address);
    boolean existsByPlaceName(String placeName);
    List<PhotoBooth> findByBrandName(BrandType brandName);
    PhotoBooth findByPlaceName(String placeName);
}
