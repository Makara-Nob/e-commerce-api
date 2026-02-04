package com.internal.feature.product.mapper;

import com.internal.enumation.StatusData;
import com.internal.feature.product.dto.request.ProductVariantRequestDto;
import com.internal.feature.product.dto.response.ProductVariantResponseDto;
import com.internal.feature.product.model.ProductVariant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ProductVariantMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "status", source = "status", qualifiedByName = "stringToStatus")
    ProductVariant toEntity(ProductVariantRequestDto dto);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "status", source = "status", qualifiedByName = "statusToString")
    ProductVariantResponseDto toDto(ProductVariant entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "status", source = "status", qualifiedByName = "stringToStatus")
    void updateEntityFromDto(ProductVariantRequestDto dto, @MappingTarget ProductVariant entity);

    @Named("stringToStatus")
    default StatusData stringToStatus(String status) {
        if (status == null || status.isEmpty()) {
            return StatusData.ACTIVE;
        }
        return StatusData.valueOf(status.toUpperCase());
    }

    @Named("statusToString")
    default String statusToString(StatusData status) {
        return status != null ? status.name() : null;
    }
}
