package com.internal.feature.cart.controller;

import com.internal.exceptions.response.ApiResponse;
import com.internal.feature.cart.dto.request.CartItemRequestDto;
import com.internal.feature.cart.dto.response.CartResponseDto;
import com.internal.feature.cart.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/items")
    public ApiResponse<CartResponseDto> addToCart(@RequestBody @Valid CartItemRequestDto requestDto) {
        return ApiResponse.success("Item added to cart", cartService.addToCart(requestDto));
    }

    @GetMapping
    public ApiResponse<CartResponseDto> getMyCart() {
        return ApiResponse.success("Cart retrieved successfully", cartService.getMyCart());
    }

    @PutMapping("/items/{itemId}")
    public ApiResponse<CartResponseDto> updateItemQuantity(
            @PathVariable Long itemId,
            @RequestParam Integer quantity) {
        return ApiResponse.success("Cart updated successfully", cartService.updateItemQuantity(itemId, quantity));
    }

    @DeleteMapping("/items/{itemId}")
    public ApiResponse<CartResponseDto> removeItem(@PathVariable Long itemId) {
        return ApiResponse.success("Item removed from cart", cartService.removeItem(itemId));
    }

    @DeleteMapping
    public ApiResponse<String> clearCart() {
        cartService.clearCart();
        return ApiResponse.success("Cart cleared successfully", null);
    }
}
