package com.internal.feature.supplier.mapper;

import com.internal.feature.supplier.dto.request.CreateSupplierRequestDto;
import com.internal.feature.supplier.dto.request.UpdateSupplierRequestDto;
import com.internal.feature.supplier.dto.response.AllSupplierResponseDto;
import com.internal.feature.supplier.dto.response.SupplierResponseDto;
import com.internal.feature.supplier.model.Supplier;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SupplierMapper {

    @Mapping(source = "status", target = "status", qualifiedByName = "statusToString")
    SupplierResponseDto toDto(Supplier supplier);
    
    @Named("statusToString")
    default String statusToString(com.internal.enumation.StatusData status) {
        return status != null ? status.name() : null;
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    Supplier toEntity(CreateSupplierRequestDto dto);

    // Update entity from DTO
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(UpdateSupplierRequestDto dto, @MappingTarget Supplier entity);

    @Named("mapToListDto")
    default List<SupplierResponseDto> mapToListDto(Page<Supplier> suppliers) {
        return suppliers.stream().map(this::toDto).toList();
    }

    @Named("mapToPaginateDto")
    default AllSupplierResponseDto mapToPaginateDto(List<SupplierResponseDto> content, Page<Supplier> suppliers) {
        AllSupplierResponseDto supplierResponseDto = new AllSupplierResponseDto();

        supplierResponseDto.setContent(content);
        supplierResponseDto.setPageNo(suppliers.getNumber() + 1);
        supplierResponseDto.setPageSize(suppliers.getSize());
        supplierResponseDto.setTotalElements(suppliers.getTotalElements());
        supplierResponseDto.setTotalPages(suppliers.getTotalPages());
        supplierResponseDto.setLast(suppliers.isLast());
        return supplierResponseDto;
    }
}

