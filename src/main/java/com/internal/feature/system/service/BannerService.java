package com.internal.feature.system.service;

import com.internal.feature.system.dto.request.BannerFilterRequestDto;
import com.internal.feature.system.dto.request.BannerRequestDto;
import com.internal.feature.system.dto.response.BannerResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BannerService {
    BannerResponseDto createBanner(BannerRequestDto requestDto);

    BannerResponseDto updateBanner(Long id, BannerRequestDto requestDto);

    void deleteBanner(Long id);

    BannerResponseDto getBannerById(Long id);

    Page<BannerResponseDto> getAllBanners(BannerFilterRequestDto filterDto);

    List<BannerResponseDto> getActiveBanners();
}
