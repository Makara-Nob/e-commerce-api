package com.internal.feature.cart.service.impl;

import com.internal.feature.auth.models.UserEntity;
import com.internal.feature.auth.repository.UserRepository;
import com.internal.feature.cart.dto.request.CartItemRequestDto;
import com.internal.feature.cart.dto.response.CartResponseDto;
import com.internal.feature.cart.mapper.CartMapper;
import com.internal.feature.cart.model.Cart;
import com.internal.feature.cart.model.CartItem;
import com.internal.feature.cart.repository.CartItemRepository;
import com.internal.feature.cart.repository.CartRepository;
import com.internal.feature.cart.service.CartService;
import com.internal.feature.product.model.Product;
import com.internal.feature.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartMapper cartMapper;

    private UserEntity getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private Cart getOrCreateCart(UserEntity user) {
        return cartRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setUser(user);
                    return cartRepository.save(cart);
                });
    }

    @Override
    @Transactional
    public CartResponseDto addToCart(CartItemRequestDto requestDto) {
        UserEntity user = getCurrentUser();
        Cart cart = getOrCreateCart(user);
        Product product = productRepository.findById(requestDto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getQuantity() < requestDto.getQuantity()) {
            throw new RuntimeException("Insufficient stock");
        }

        Optional<CartItem> existingItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), product.getId());

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + requestDto.getQuantity());
            cartItemRepository.save(item);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(requestDto.getQuantity());
            cartItemRepository.save(newItem);
            cart.getItems().add(newItem); // Update in-memory list for immediate mapping
        }

        // Refresh cart to get updated items if needed, or just return mapped cart
        // Since we modified items, re-fetching or relying on Hibernate cache
        return cartMapper.toDto(cart);
    }

    @Override
    public CartResponseDto getMyCart() {
        UserEntity user = getCurrentUser();
        Cart cart = getOrCreateCart(user);
        return cartMapper.toDto(cart);
    }

    @Override
    @Transactional
    public CartResponseDto updateItemQuantity(Long cartItemId, Integer quantity) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        // Ensure user owns this item
        UserEntity user = getCurrentUser();
        if (!item.getCart().getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        if (quantity <= 0) {
            cartItemRepository.delete(item);
            item.getCart().getItems().remove(item);
        } else {
            if (item.getProduct().getQuantity() < quantity) {
                throw new RuntimeException("Insufficient stock");
            }
            item.setQuantity(quantity);
            cartItemRepository.save(item);
        }

        return cartMapper.toDto(item.getCart());
    }

    @Override
    @Transactional
    public CartResponseDto removeItem(Long cartItemId) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        UserEntity user = getCurrentUser();
        if (!item.getCart().getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        Cart cart = item.getCart();
        cart.getItems().remove(item);
        cartItemRepository.delete(item);

        return cartMapper.toDto(cart);
    }

    @Override
    @Transactional
    public void clearCart() {
        UserEntity user = getCurrentUser();
        Cart cart = getOrCreateCart(user);
        cart.getItems().clear();
        cartRepository.save(cart); // Cascade delete orphan removal handles items
    }
}
