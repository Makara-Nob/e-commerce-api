package com.internal.feature.system.service.impl;

import com.internal.enumation.StatusData;
import com.internal.feature.system.dto.request.BannerFilterRequestDto;
import com.internal.feature.system.dto.request.BannerRequestDto;
import com.internal.feature.system.dto.response.BannerResponseDto;
import com.internal.feature.system.mapper.BannerMapper;
import com.internal.feature.system.model.Banner;
import com.internal.feature.system.repository.BannerRepository;
import com.internal.feature.system.service.BannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BannerServiceImpl implements BannerService {

    private final BannerRepository bannerRepository;
    private final BannerMapper bannerMapper;

    @Override
    @Transactional
    public BannerResponseDto createBanner(BannerRequestDto requestDto) {
        Banner banner = bannerMapper.toEntity(requestDto);
        if (banner.getStatus() == null) {
            banner.setStatus(StatusData.ACTIVE);
        }
        if (banner.getDisplayOrder() == null) {
            banner.setDisplayOrder(0);
        }
        return bannerMapper.toDto(bannerRepository.save(banner));
    }

    @Override
    @Transactional
    public BannerResponseDto updateBanner(Long id, BannerRequestDto requestDto) {
        Banner banner = bannerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Banner not found with ID: " + id));
        bannerMapper.updateEntity(requestDto, banner);
        return bannerMapper.toDto(bannerRepository.save(banner));
    }

    @Override
    @Transactional
    public void deleteBanner(Long id) {
        if (!bannerRepository.existsById(id)) {
            throw new RuntimeException("Banner not found with ID: " + id);
        }
        bannerRepository.deleteById(id);
    }

    @Override
    public BannerResponseDto getBannerById(Long id) {
        Banner banner = bannerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Banner not found with ID: " + id));
        return bannerMapper.toDto(banner);
    }

    @Override
    public Page<BannerResponseDto> getAllBanners(BannerFilterRequestDto filterDto) {
        Sort sort = filterDto.getSortDirection().equalsIgnoreCase("asc")
                ? Sort.by(filterDto.getSortBy()).ascending()
                : Sort.by(filterDto.getSortBy()).descending();
        Pageable pageable = PageRequest.of(filterDto.getPage(), filterDto.getSize(), sort);
        return bannerRepository.findAll(pageable).map(bannerMapper::toDto);
    }

    @Override
    public List<BannerResponseDto> getActiveBanners() {
        return bannerRepository.findAllByStatusOrderByDisplayOrderAsc(StatusData.ACTIVE)
                .stream()
                .map(bannerMapper::toDto)
                .toList();
    }
}
