package com.internal.feature.order.service;

import com.internal.enumation.OrderStatus;
import com.internal.feature.order.dto.request.OrderFilterRequestDto;
import com.internal.feature.order.dto.request.OrderRequestDto;
import com.internal.feature.order.dto.response.AllOrderResponseDto;
import com.internal.feature.order.dto.response.OrderResponseDto;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderResponseDto createOrder(OrderRequestDto requestDto);

    AllOrderResponseDto getAllOrders(OrderFilterRequestDto filterDto);

    AllOrderResponseDto getMyOrders(OrderFilterRequestDto filterDto);

    OrderResponseDto getOrderById(Long id);

    OrderResponseDto updateOrderIdStatus(Long id, OrderStatus status);
}
