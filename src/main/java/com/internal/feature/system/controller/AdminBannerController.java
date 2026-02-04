package com.internal.feature.system.controller;

import com.internal.exceptions.response.ApiResponse;
import com.internal.feature.system.dto.request.BannerFilterRequestDto;
import com.internal.feature.system.dto.request.BannerRequestDto;
import com.internal.feature.system.dto.response.BannerResponseDto;
import com.internal.feature.system.service.BannerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/banners")
@RequiredArgsConstructor
public class AdminBannerController {

    private final BannerService bannerService;

    @PostMapping
    public ApiResponse<BannerResponseDto> createBanner(@RequestBody @Valid BannerRequestDto requestDto) {
        return ApiResponse.success("Banner created successfully", bannerService.createBanner(requestDto));
    }

    @PutMapping("/{id}")
    public ApiResponse<BannerResponseDto> updateBanner(
            @PathVariable Long id,
            @RequestBody @Valid BannerRequestDto requestDto) {
        return ApiResponse.success("Banner updated successfully", bannerService.updateBanner(id, requestDto));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteBanner(@PathVariable Long id) {
        bannerService.deleteBanner(id);
        return ApiResponse.success("Banner deleted successfully", null);
    }

    @GetMapping("/{id}")
    public ApiResponse<BannerResponseDto> getBannerById(@PathVariable Long id) {
        return ApiResponse.success("Banner retrieved successfully", bannerService.getBannerById(id));
    }

    @PostMapping("/search")
    public ApiResponse<Page<BannerResponseDto>> getAllBanners(@RequestBody BannerFilterRequestDto filterDto) {
        return ApiResponse.success("Banners retrieved successfully", bannerService.getAllBanners(filterDto));
    }
}
