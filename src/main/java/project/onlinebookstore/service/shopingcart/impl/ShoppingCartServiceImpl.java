package project.onlinebookstore.service.shopingcart.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.onlinebookstore.dto.cartitem.CreateCartItemRequestDto;
import project.onlinebookstore.dto.cartitem.CreateCartItemUpdateRequestDto;
import project.onlinebookstore.dto.shoppingcart.ShoppingCartDto;
import project.onlinebookstore.exception.EntityNotFoundException;
import project.onlinebookstore.mapper.CartItemMapper;
import project.onlinebookstore.mapper.ShoppingCartMapper;
import project.onlinebookstore.model.Book;
import project.onlinebookstore.model.CartItem;
import project.onlinebookstore.model.ShoppingCart;
import project.onlinebookstore.model.User;
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
    public ShoppingCartDto getShoppingCart(Long userId) {
        ShoppingCart shoppingCart = getShoppingCartForUser(userId);
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    @Transactional
    public ShoppingCartDto addCartItemToShoppingCart(
            Long userId,
            CreateCartItemRequestDto cartItemRequestDto) {
        ShoppingCart shoppingCart = getShoppingCartForUser(userId);

        Book book = bookRepository.findById(cartItemRequestDto.bookId())
                .orElseThrow(() -> new EntityNotFoundException("Book with Id: "
                        + cartItemRequestDto.bookId()
                        + " not found"));

        cartItemRepository.findByShoppingCartIdAndBookId(shoppingCart.getId(),
                        cartItemRequestDto.bookId())
                .ifPresent(existingCartItem -> {
                    throw new EntityNotFoundException("Book with Id: "
                            + cartItemRequestDto.bookId()
                            + " already exists in the shopping cart");
                });

        CartItem cartItems = cartItemMapper.toModel(cartItemRequestDto);
        cartItems.setBook(book);
        cartItems.setShoppingCart(shoppingCart);
        cartItemRepository.save(cartItems);

        shoppingCart.getCartItems().add(cartItems);

        return shoppingCartMapper.toDto(shoppingCartRepository.save(shoppingCart));
    }

    @Override
    @Transactional
    public ShoppingCartDto updateCartItemInShoppingCart(
            Long userId,
            Long cartItemId,
            CreateCartItemUpdateRequestDto updateRequestDto) {
        ShoppingCart shoppingCart = getShoppingCartForUser(userId);

        CartItem cartItem =
                cartItemRepository.findByIdAndShoppingCartId(cartItemId, shoppingCart.getId())
                        .map(item -> {
                            item.setQuantity(updateRequestDto.quantity());
                            return item;
                        })
                        .orElseThrow(() -> new EntityNotFoundException(
                                String.format("No cart item with id: %d for user: %d",
                                        cartItemId, userId)
                        ));
        cartItemRepository.save(cartItem);

        return shoppingCartMapper.toDto(shoppingCartRepository.save(shoppingCart));
    }

    @Override
    @Transactional
    public void removeCartItemFromShoppingCart(Long userId, Long cartItemId) {
        ShoppingCart shoppingCart = getShoppingCartForUser(userId);
        CartItem cartItem = getCartItemById(cartItemId);

        shoppingCart.getCartItems().remove(cartItem);
        cartItem.setShoppingCart(shoppingCart);

        cartItemRepository.delete(cartItem);
    }

    @Override
    public void createShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
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
