package com.internal.feature.cart.mapper;

import com.internal.feature.cart.dto.response.CartItemResponseDto;
import com.internal.feature.cart.dto.response.CartResponseDto;
import com.internal.feature.cart.model.Cart;
import com.internal.feature.cart.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface CartMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "items", target = "items")
    CartResponseDto toDto(Cart cart);

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "product.sku", target = "productSku")
    @Mapping(target = "productImageUrl", ignore = true)
    @Mapping(source = "product.sellingPrice", target = "unitPrice")
    @Mapping(target = "subTotal", expression = "java(calculateSubTotal(cartItem))")
    CartItemResponseDto toItemDto(CartItem cartItem);

    default BigDecimal calculateSubTotal(CartItem cartItem) {
        if (cartItem.getProduct() == null || cartItem.getProduct().getSellingPrice() == null) {
            return BigDecimal.ZERO;
        }
        return cartItem.getProduct().getSellingPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));
    }
}
