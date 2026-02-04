package com.internal.feature.product.mapper;

import com.internal.feature.product.dto.request.BrandRequestDto;
import com.internal.feature.product.dto.response.AllBrandResponseDto;
import com.internal.feature.product.dto.response.BrandResponseDto;
import com.internal.feature.product.model.Brand;
import org.mapstruct.*;
import org.springframework.data.domain.Page;
import java.util.List;

@Mapper(componentModel = "spring")
public interface BrandMapper {

    BrandResponseDto toDto(Brand brand);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    Brand toEntity(BrandRequestDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(BrandRequestDto dto, @MappingTarget Brand entity);

    @Named("mapToListDto")
    default List<BrandResponseDto> mapToListDto(Page<Brand> brands) {
        return brands.stream().map(this::toDto).toList();
    }

    @Named("mapToPaginateDto")
    default AllBrandResponseDto mapToPaginateDto(List<BrandResponseDto> content, Page<Brand> brands) {
        AllBrandResponseDto brandResponseDto = new AllBrandResponseDto();

        brandResponseDto.setContent(content);
        brandResponseDto.setPageNo(brands.getNumber() + 1);
        brandResponseDto.setPageSize(brands.getSize());
        brandResponseDto.setTotalElements(brands.getTotalElements());
        brandResponseDto.setTotalPages(brands.getTotalPages());
        brandResponseDto.setLast(brands.isLast());
        return brandResponseDto;
    }
}
