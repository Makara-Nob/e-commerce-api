package com.internal.feature.system.dto.request;

import com.internal.enumation.StatusData;
import lombok.Data;

@Data
public class BannerFilterRequestDto {
    private int page = 0;
    private int size = 10;
    private String sortBy = "displayOrder";
    private String sortDirection = "asc";

    // Optional filters for future use
    private String search; // Title or description
    private StatusData status;
}
