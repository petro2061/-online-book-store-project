package project.onlinebookstore.service.shopingcart;

import project.onlinebookstore.dto.cartitem.CreateCartItemRequestDto;
import project.onlinebookstore.dto.cartitem.CreateCartItemUpdateRequestDto;
import project.onlinebookstore.dto.shoppingcart.ShoppingCartDto;
import project.onlinebookstore.model.User;

public interface ShoppingCartService {
    ShoppingCartDto getShoppingCart(Long userId);

    ShoppingCartDto addCartItemToShoppingCart(
            Long userId,
            CreateCartItemRequestDto cartItemRequestDto);

    ShoppingCartDto updateCartItemInShoppingCart(
            Long userId,
            Long cartItemId,
            CreateCartItemUpdateRequestDto updateRequestDto);

    void removeCartItemFromShoppingCart(Long userId, Long cartItemId);

    void createShoppingCart(User user);
}
