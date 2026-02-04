package com.internal.feature.product.dto.request;

import com.internal.enumation.StatusData;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BrandRequestDto {
    @NotBlank(message = "Brand name is required")
    private String name;

    private String description;

    private String logoUrl;

    private StatusData status;
}
