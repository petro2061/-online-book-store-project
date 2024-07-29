package project.onlinebookstore.service.shopingcart.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.onlinebookstore.dto.cartitem.CartItemDto;
import project.onlinebookstore.dto.cartitem.CreateCartItemRequestDto;
import project.onlinebookstore.dto.cartitem.CreateCartItemUpdateRequestDto;
import project.onlinebookstore.dto.shoppingcart.ShoppingCartDto;
import project.onlinebookstore.mapper.CartItemMapper;
import project.onlinebookstore.mapper.ShoppingCartMapper;
import project.onlinebookstore.model.Book;
import project.onlinebookstore.model.CartItem;
import project.onlinebookstore.model.ShoppingCart;
import project.onlinebookstore.repository.book.BookRepository;
import project.onlinebookstore.repository.cartitem.CartItemRepository;
import project.onlinebookstore.repository.shoppingcart.ShoppingCartRepository;
import project.onlinebookstore.service.shopingcart.ShoppingCartService;

@RequiredArgsConstructor
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemMapper cartItemMapper;

    @Override
    @Transactional
    public ShoppingCartDto getShoppingCart(Long userId) {
        ShoppingCart shoppingCar = getShoppingCartForUser(userId);

        List<CartItemDto> cartItem = shoppingCar.getCartItems()
                .stream()
                .map(cartItemMapper::toDto)
                .toList();

        ShoppingCartDto shoppingCartDto = shoppingCartMapper.toDto(shoppingCar);
        shoppingCartDto.setCartItems(cartItem);

        return shoppingCartDto;
    }

    @Override
    @Transactional
    public CartItemDto addCartItemToShoppingCart(
            Long userId,
            CreateCartItemRequestDto cartItemRequestDto) {
        ShoppingCart shoppingCar = getShoppingCartForUser(userId);

        cartItemRepository.findByShoppingCartIdAndBookId(shoppingCar.getId(),
                        cartItemRequestDto.bookId())
                .ifPresent(existingCartItem -> {
                    throw new EntityNotFoundException("Book with Id: "
                            + cartItemRequestDto.bookId()
                            + " already exists in the shopping cart");
                });

        Book book = bookRepository.findById(cartItemRequestDto.bookId())
                .orElseThrow(() -> new EntityNotFoundException("Book with Id: "
                        + cartItemRequestDto.bookId()
                        + " not found"));

        CartItem cartItems = cartItemMapper.toModel(cartItemRequestDto);
        cartItems.setBook(book);
        cartItems.setShoppingCart(shoppingCar);
        cartItemRepository.save(cartItems);

        shoppingCar.getCartItems().add(cartItems);

        return cartItemMapper.toDto(cartItems);
    }

    @Override
    @Transactional
    public CartItemDto updateCartItemInShoppingCart(
            Long userId,
            Long cartItemId,
            CreateCartItemUpdateRequestDto updateRequestDto) {
        ShoppingCart shoppingCart = getShoppingCartForUser(userId);
        CartItem cartItem = getCartItemById(cartItemId);

        cartItem.setQuantity(updateRequestDto.quantity());
        cartItem.setShoppingCart(shoppingCart);
        cartItemRepository.save(cartItem);

        return cartItemMapper.toDto(cartItem);
    }

    @Override
    @Transactional
    public CartItemDto removeCartItemFromShoppingCart(Long userId, Long cartItemId) {
        ShoppingCart shoppingCart = getShoppingCartForUser(userId);
        CartItem cartItem = getCartItemById(cartItemId);

        shoppingCart.getCartItems().remove(cartItem);
        cartItem.setShoppingCart(shoppingCart);

        cartItemRepository.delete(cartItem);

        return cartItemMapper.toDto(cartItem);
    }

    private ShoppingCart getShoppingCartForUser(Long userId) {
        return shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Can't find shopping cart for user with Id: "
                                        + userId));
    }

    private CartItem getCartItemById(Long cartItemId) {
        return cartItemRepository.findById(cartItemId).orElseThrow(() ->
                new EntityNotFoundException("Can't find cart item by Id: "
                        + cartItemId));
    }
}
