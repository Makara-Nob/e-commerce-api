package com.internal.feature.system.controller;

import com.internal.exceptions.response.ApiResponse;
import com.internal.feature.system.dto.response.BannerResponseDto;
import com.internal.feature.system.service.BannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/public/banners")
@RequiredArgsConstructor
public class PublicBannerController {

    private final BannerService bannerService;

    @GetMapping
    public ApiResponse<List<BannerResponseDto>> getActiveBanners() {
        return ApiResponse.success("Active banners retrieved successfully", bannerService.getActiveBanners());
    }
}
