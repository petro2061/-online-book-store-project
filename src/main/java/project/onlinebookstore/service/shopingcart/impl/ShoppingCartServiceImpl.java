package project.onlinebookstore.service.shopingcart.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.onlinebookstore.dto.cartitem.CreateCartItemRequestDto;
import project.onlinebookstore.dto.shoppingcart.ShoppingCartDto;
import project.onlinebookstore.mapper.CartItemMapper;
import project.onlinebookstore.mapper.ShoppingCartMapper;
import project.onlinebookstore.model.CartItem;
import project.onlinebookstore.model.ShoppingCart;
import project.onlinebookstore.repository.cartitem.CartItemRepository;
import project.onlinebookstore.repository.shoppingcart.ShoppingCartRepository;
import project.onlinebookstore.service.shopingcart.ShoppingCartService;

@RequiredArgsConstructor
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemMapper cartItemMapper;

    @Override
    public ShoppingCartDto getShoppingCart(Long userId) {
        ShoppingCart shoppingCar = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Can't find shopping cart for user with Id: "
                                        + userId));
        return shoppingCartMapper.toDto(shoppingCar);
    }

    @Override
    @Transactional
    public ShoppingCartDto addBookToShoppingCart(
            Long userId,
            CreateCartItemRequestDto cartItemRequestDto) {
        ShoppingCart shoppingCar = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Can't find shopping cart for user with Id: "
                                        + userId));
        CartItem cartItems = cartItemMapper.toModel(cartItemRequestDto);

        cartItems.setShoppingCart(shoppingCar);
        shoppingCar.setCartItems(Set.of(cartItems));

        ShoppingCart savedShoppingCart = shoppingCartRepository.save(shoppingCar);

        return shoppingCartMapper.toDto(savedShoppingCart);
    }

    @Override
    @Transactional
    public ShoppingCartDto updateCartItem(Long cartItemId, int quantity) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(() ->
                new EntityNotFoundException("Can't find cartItem by Id: " + cartItemId));

        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);

        ShoppingCart shoppingCart = cartItem.getShoppingCart();

        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    @Transactional
    public ShoppingCartDto removeCartItem(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(() ->
                new EntityNotFoundException("Can't find cartItem by Id: " + cartItemId));

        ShoppingCart shoppingCart = cartItem.getShoppingCart();

        shoppingCart.getCartItems().remove(cartItem);
        cartItemRepository.delete(cartItem);

        return shoppingCartMapper.toDto(shoppingCart);
    }
}
