package com.internal.feature.order.controller;

import com.internal.enumation.OrderStatus;
import com.internal.exceptions.response.ApiResponse;
import com.internal.feature.order.dto.request.OrderFilterRequestDto;
import com.internal.feature.order.dto.response.AllOrderResponseDto;
import com.internal.feature.order.dto.response.OrderResponseDto;
import com.internal.feature.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/orders")
@RequiredArgsConstructor
public class AdminOrderController {

    private final OrderService orderService;

    @PostMapping
    public ApiResponse<AllOrderResponseDto> getAllOrders(@RequestBody OrderFilterRequestDto filterDto) {
        return ApiResponse.success("All orders retrieved successfully", orderService.getAllOrders(filterDto));
    }

    @PutMapping("/{id}/status")
    public ApiResponse<OrderResponseDto> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus status) {
        return ApiResponse.success("Order status updated successfully", orderService.updateOrderIdStatus(id, status));
    }
}
