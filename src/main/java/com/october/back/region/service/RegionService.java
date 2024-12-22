package com.october.back.region.service;

import com.october.back.region.entity.Region;
import com.october.back.region.repository.RegisonRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RegionService {
    private final RegisonRepository regisonRepository;

    public List<Region> findAll() {
        return regisonRepository.findAll();
    }
}
