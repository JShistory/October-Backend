package com.october.back.photobooth.repository;

import com.october.back.photobooth.entity.PhotoBooth;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoBoothRepository extends JpaRepository<PhotoBooth, Long> {
    PhotoBooth findByPlaceNameAndAddress(String placeName, String address);
    boolean existsByPlaceName(String placeName);
}
