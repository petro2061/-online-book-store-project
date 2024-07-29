package project.onlinebookstore.service.shopingcart;

import project.onlinebookstore.dto.cartitem.CartItemDto;
import project.onlinebookstore.dto.cartitem.CreateCartItemRequestDto;
import project.onlinebookstore.dto.cartitem.CreateCartItemUpdateRequestDto;
import project.onlinebookstore.dto.shoppingcart.ShoppingCartDto;

public interface ShoppingCartService {
    ShoppingCartDto getShoppingCart(Long userId);

    CartItemDto addCartItemToShoppingCart(
            Long userId,
            CreateCartItemRequestDto cartItemRequestDto);

    CartItemDto updateCartItemInShoppingCart(
            Long userId,
            Long cartItemId,
            CreateCartItemUpdateRequestDto updateRequestDto);

    CartItemDto removeCartItemFromShoppingCart(Long userId, Long cartItemId);
}
