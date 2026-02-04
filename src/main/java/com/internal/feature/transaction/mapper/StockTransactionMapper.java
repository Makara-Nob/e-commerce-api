package com.internal.feature.transaction.mapper;

import com.internal.feature.product.mapper.ProductMapper;
import com.internal.feature.transaction.dto.request.CreateStockTransactionRequestDto;
import com.internal.feature.transaction.dto.response.AllStockTransactionResponseDto;
import com.internal.feature.transaction.dto.response.StockTransactionResponseDto;
import com.internal.feature.transaction.model.StockTransaction;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface StockTransactionMapper {

    @Mapping(source = "type", target = "type", qualifiedByName = "typeToString")
    @Mapping(source = "product", target = "product")
    StockTransactionResponseDto toDto(StockTransaction transaction);

    @Named("typeToString")
    default String typeToString(com.internal.enumation.TransactionType type) {
        return type != null ? type.name() : null;
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "previousStock", ignore = true)
    @Mapping(target = "newStock", ignore = true)
    @Mapping(target = "transactionDate", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    StockTransaction toEntity(CreateStockTransactionRequestDto dto);

    @Named("mapToListDto")
    default List<StockTransactionResponseDto> mapToListDto(Page<StockTransaction> transactions) {
        return transactions.stream().map(this::toDto).toList();
    }

    @Named("mapToPaginateDto")
    default AllStockTransactionResponseDto mapToPaginateDto(List<StockTransactionResponseDto> content, 
                                                            Page<StockTransaction> transactions) {
        AllStockTransactionResponseDto responseDto = new AllStockTransactionResponseDto();
        responseDto.setContent(content);
        responseDto.setPageNo(transactions.getNumber() + 1);
        responseDto.setPageSize(transactions.getSize());
        responseDto.setTotalElements(transactions.getTotalElements());
        responseDto.setTotalPages(transactions.getTotalPages());
        responseDto.setLast(transactions.isLast());
        return responseDto;
    }
}