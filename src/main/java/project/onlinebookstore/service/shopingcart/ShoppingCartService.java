package project.onlinebookstore.service.shopingcart;

import project.onlinebookstore.dto.cartitem.CreateCartItemRequestDto;
import project.onlinebookstore.dto.shoppingcart.ShoppingCartDto;

public interface ShoppingCartService {
    ShoppingCartDto getShoppingCart(Long userId);

    ShoppingCartDto addBookToShoppingCart(
            Long userId,
            CreateCartItemRequestDto cartItemRequestDto);

    ShoppingCartDto updateCartItem(Long cartItemId, int quantity);

    ShoppingCartDto removeCartItem(Long cartItemId);
}
