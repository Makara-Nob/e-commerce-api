package com.internal.feature.order.controller;

import com.internal.exceptions.response.ApiResponse;
import com.internal.feature.order.dto.request.OrderFilterRequestDto;
import com.internal.feature.order.dto.request.OrderRequestDto;
import com.internal.feature.order.dto.response.AllOrderResponseDto;
import com.internal.feature.order.dto.response.OrderResponseDto;
import com.internal.feature.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ApiResponse<OrderResponseDto> createOrder(@RequestBody @Valid OrderRequestDto requestDto) {
        return ApiResponse.success("Order created successfully", orderService.createOrder(requestDto));
    }

    @PostMapping("/my-orders")
    public ApiResponse<AllOrderResponseDto> getMyOrders(@RequestBody OrderFilterRequestDto filterDto) {
        return ApiResponse.success("Orders retrieved successfully", orderService.getMyOrders(filterDto));
    }

    @GetMapping("/{id}")
    public ApiResponse<OrderResponseDto> getOrderById(@PathVariable Long id) {
        return ApiResponse.success("Order retrieved successfully", orderService.getOrderById(id));
    }
}
