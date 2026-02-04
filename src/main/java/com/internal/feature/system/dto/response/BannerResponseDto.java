package com.internal.feature.system.dto.response;

import com.internal.enumation.StatusData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BannerResponseDto {
    private Long id;
    private String title;
    private String imageUrl;
    private String linkUrl;
    private String description;
    private Integer displayOrder;
    private StatusData status;
}
