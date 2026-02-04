package com.internal.feature.order.dto.request;

import com.internal.enumation.OrderStatus;
import lombok.Data;

@Data
public class OrderFilterRequestDto {
    private int page = 0;
    private int size = 10;
    private String sortBy = "id";
    private String sortDirection = "desc";

    // Optional filters for future use or immediate implementation
    private String search; // Invoice number or username
    private OrderStatus status;
}
