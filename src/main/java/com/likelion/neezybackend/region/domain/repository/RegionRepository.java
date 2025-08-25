package com.likelion.neezybackend.region.domain.repository;

import com.likelion.neezybackend.region.domain.Region;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RegionRepository extends JpaRepository<Region, Long> {

    Optional<Region> findByRegionNameIgnoreCase(String regionName);

}
