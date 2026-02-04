package com.internal.feature.cart.service;

import com.internal.feature.cart.dto.request.CartItemRequestDto;
import com.internal.feature.cart.dto.response.CartResponseDto;

public interface CartService {
    CartResponseDto addToCart(CartItemRequestDto requestDto);

    CartResponseDto getMyCart();

    CartResponseDto updateItemQuantity(Long cartItemId, Integer quantity);

    CartResponseDto removeItem(Long cartItemId);

    void clearCart();
}
