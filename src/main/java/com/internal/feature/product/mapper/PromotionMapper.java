package com.internal.feature.product.mapper;

import com.internal.enumation.DiscountType;
import com.internal.enumation.StatusData;
import com.internal.feature.product.dto.request.PromotionRequestDto;
import com.internal.feature.product.dto.response.PromotionResponseDto;
import com.internal.feature.product.model.Promotion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = { ProductMapper.class })
public interface PromotionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "discountType", source = "discountType", qualifiedByName = "stringToDiscountType")
    @Mapping(target = "status", source = "status", qualifiedByName = "stringToStatus")
    Promotion toEntity(PromotionRequestDto dto);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "discountType", source = "discountType", qualifiedByName = "discountTypeToString")
    @Mapping(target = "status", source = "status", qualifiedByName = "statusToString")
    @Mapping(source = "product", target = "product")
    PromotionResponseDto toDto(Promotion promotion);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "discountType", source = "discountType", qualifiedByName = "stringToDiscountType")
    @Mapping(target = "status", source = "status", qualifiedByName = "stringToStatus")
    void updateEntityFromDto(PromotionRequestDto dto, @MappingTarget Promotion entity);

    @Named("stringToDiscountType")
    default DiscountType stringToDiscountType(String discountType) {
        if (discountType == null || discountType.isEmpty()) {
            return DiscountType.PERCENTAGE;
        }
        return DiscountType.valueOf(discountType.toUpperCase());
    }

    @Named("discountTypeToString")
    default String discountTypeToString(DiscountType discountType) {
        return discountType != null ? discountType.name() : null;
    }

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
