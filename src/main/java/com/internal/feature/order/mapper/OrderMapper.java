package com.internal.feature.order.mapper;

import com.internal.feature.order.dto.request.OrderItemRequestDto;
import com.internal.feature.order.dto.request.OrderRequestDto;
import com.internal.feature.order.dto.response.AllOrderResponseDto;
import com.internal.feature.order.dto.response.OrderItemResponseDto;
import com.internal.feature.order.dto.response.OrderResponseDto;
import com.internal.feature.order.model.Order;
import com.internal.feature.order.model.OrderItem;
import com.internal.feature.product.model.Product;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.username", target = "userName")
    @Mapping(source = "items", target = "items")
    OrderResponseDto toDto(Order order);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "invoiceNumber", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "totalAmount", ignore = true)
    @Mapping(target = "discountAmount", ignore = true)
    @Mapping(target = "netAmount", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "items", ignore = true)
    Order toEntity(OrderRequestDto dto);

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "product.sku", target = "productSku")
    @Mapping(target = "productImageUrl", ignore = true)
    OrderItemResponseDto toItemDto(OrderItem item);

    @Named("mapToPaginateDto")
    default AllOrderResponseDto mapToPaginateDto(Page<Order> orders) {
        AllOrderResponseDto responseDto = new AllOrderResponseDto();
        responseDto.setContent(orders.stream().map(this::toDto).toList());
        responseDto.setPageNo(orders.getNumber() + 1);
        responseDto.setPageSize(orders.getSize());
        responseDto.setTotalElements(orders.getTotalElements());
        responseDto.setTotalPages(orders.getTotalPages());
        responseDto.setLast(orders.isLast());
        return responseDto;
    }
}
