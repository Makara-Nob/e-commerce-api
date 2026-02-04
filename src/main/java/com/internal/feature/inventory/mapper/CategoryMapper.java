package com.internal.feature.inventory.mapper;

import com.internal.feature.inventory.dto.request.CategoryRequestDTO;
import com.internal.feature.inventory.dto.request.CategoryUpdateDto;
import com.internal.feature.inventory.dto.response.AllCategoryResponseDto;
import com.internal.feature.inventory.dto.response.CategoryDTO;
import com.internal.feature.inventory.model.Category;
import org.mapstruct.*;
import org.springframework.data.domain.Page;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryDTO toDto(Category category);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    Category toEntity(CategoryRequestDTO dto);

    // Update entity from DTO
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(CategoryUpdateDto dto, @MappingTarget Category entity);


    @Named("mapToListDto")
    default List<CategoryDTO> mapToListDto(Page<Category> categories) {
        return categories.stream().map(this::toDto).toList();
    }

    @Named("mapToPaginateDto")
    default AllCategoryResponseDto mapToPaginateDto(List<CategoryDTO> content, Page<Category> categories) {
        AllCategoryResponseDto categoryResponseDto = new AllCategoryResponseDto();

        categoryResponseDto.setContent(content);
        categoryResponseDto.setPageNo(categories.getNumber() + 1);
        categoryResponseDto.setPageSize(categories.getSize());
        categoryResponseDto.setTotalElements(categories.getTotalElements());
        categoryResponseDto.setTotalPages(categories.getTotalPages());
        categoryResponseDto.setLast(categories.isLast());
        return categoryResponseDto;
    }
}
