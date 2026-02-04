package com.internal.feature.system.repository;

import com.internal.enumation.StatusData;
import com.internal.feature.system.model.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BannerRepository extends JpaRepository<Banner, Long> {
    List<Banner> findAllByStatusOrderByDisplayOrderAsc(StatusData status);
}
