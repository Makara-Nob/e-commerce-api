package com.internal.feature.order.service.impl;

import com.internal.enumation.OrderStatus;
import com.internal.enumation.TransactionType;
import com.internal.feature.auth.models.UserEntity;
import com.internal.feature.auth.repository.UserRepository;
import com.internal.feature.order.dto.request.OrderFilterRequestDto;
import com.internal.feature.order.dto.request.OrderItemRequestDto;
import com.internal.feature.order.dto.request.OrderRequestDto;
import com.internal.feature.order.dto.response.AllOrderResponseDto;
import com.internal.feature.order.dto.response.OrderResponseDto;
import com.internal.feature.order.mapper.OrderMapper;
import com.internal.feature.order.model.Order;
import com.internal.feature.order.model.OrderItem;
import com.internal.feature.order.repository.OrderRepository;
import com.internal.feature.order.service.OrderService;
import com.internal.feature.product.model.Product;
import com.internal.feature.product.repository.ProductRepository;
import com.internal.feature.transaction.dto.request.CreateStockTransactionRequestDto;
import com.internal.feature.transaction.service.StockTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final StockTransactionService stockTransactionService;

    @Override
    @Transactional
    public OrderResponseDto createOrder(OrderRequestDto requestDto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = new Order();
        order.setUser(user);
        order.setInvoiceNumber("INV-" + System.currentTimeMillis());
        order.setStatus(OrderStatus.PENDING);
        order.setPaymentMethod(requestDto.getPaymentMethod());
        order.setShippingAddress(requestDto.getShippingAddress());
        order.setNote(requestDto.getNote());
        order.setDiscountAmount(BigDecimal.ZERO); // Logic for discount can be added here

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (OrderItemRequestDto itemDto : requestDto.getItems()) {
            Product product = productRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found with ID: " + itemDto.getProductId()));

            if (product.getQuantity() < itemDto.getQuantity()) {
                throw new RuntimeException("Insufficient stock for product: " + product.getName());
            }

            BigDecimal unitPrice = product.getSellingPrice();
            BigDecimal subTotal = unitPrice.multiply(BigDecimal.valueOf(itemDto.getQuantity()));
            totalAmount = totalAmount.add(subTotal);

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemDto.getQuantity());
            orderItem.setUnitPrice(unitPrice);
            orderItem.setSubTotal(subTotal);
            orderItems.add(orderItem);

            // Deduct stock
            product.setQuantity(product.getQuantity() - itemDto.getQuantity());
            productRepository.save(product);

            // Record transaction
            stockTransactionService.createTransaction(new CreateStockTransactionRequestDto(
                    product.getId(),
                    TransactionType.STOCK_OUT,
                    itemDto.getQuantity(),
                    "Order " + order.getInvoiceNumber(),
                    "Order placed by " + username));
        }

        order.setItems(orderItems);
        order.setTotalAmount(totalAmount);
        order.setNetAmount(totalAmount.subtract(order.getDiscountAmount()));

        Order savedOrder = orderRepository.save(order);
        return orderMapper.toDto(savedOrder);
    }

    @Override
    public AllOrderResponseDto getAllOrders(OrderFilterRequestDto filterDto) {
        Sort sort = filterDto.getSortDirection().equalsIgnoreCase("asc")
                ? Sort.by(filterDto.getSortBy()).ascending()
                : Sort.by(filterDto.getSortBy()).descending();
        Pageable pageable = PageRequest.of(filterDto.getPage(), filterDto.getSize(), sort);

        Page<Order> orders = orderRepository.findAll(pageable);
        // Future: Implement search/filter logic here using Specification

        return orderMapper.mapToPaginateDto(orders);
    }

    @Override
    public AllOrderResponseDto getMyOrders(OrderFilterRequestDto filterDto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Sort sort = filterDto.getSortDirection().equalsIgnoreCase("asc")
                ? Sort.by(filterDto.getSortBy()).ascending()
                : Sort.by(filterDto.getSortBy()).descending();
        Pageable pageable = PageRequest.of(filterDto.getPage(), filterDto.getSize(), sort);

        Page<Order> orders = orderRepository.findByUserId(user.getId(), pageable);
        return orderMapper.mapToPaginateDto(orders);
    }

    @Override
    public OrderResponseDto getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + id));
        return orderMapper.toDto(order);
    }

    @Override
    public OrderResponseDto updateOrderIdStatus(Long id, OrderStatus status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + id));
        order.setStatus(status);
        Order updatedOrder = orderRepository.save(order);
        return orderMapper.toDto(updatedOrder);
    }
}
