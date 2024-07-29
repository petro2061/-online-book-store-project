package project.onlinebookstore.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import project.onlinebookstore.dto.cartitem.CartItemDto;
import project.onlinebookstore.dto.cartitem.CreateCartItemRequestDto;
import project.onlinebookstore.dto.cartitem.CreateCartItemUpdateRequestDto;
import project.onlinebookstore.dto.shoppingcart.ShoppingCartDto;
import project.onlinebookstore.model.User;
import project.onlinebookstore.service.shopingcart.ShoppingCartService;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public ShoppingCartDto getShoppingCart(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.getShoppingCart(user.getId());
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public CartItemDto addCartItemToShoppingCart(
            Authentication authentication,
            @RequestBody @Valid CreateCartItemRequestDto cartItemRequestDto) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService
                .addCartItemToShoppingCart(user.getId(), cartItemRequestDto);
    }

    @PutMapping("/items/{cartItemId}")
    public CartItemDto updateCartItemInShoppingCart(
            Authentication authentication,
            @PathVariable Long cartItemId,
            @RequestBody @Valid
            CreateCartItemUpdateRequestDto updateRequestDto) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService
                .updateCartItemInShoppingCart(user.getId(), cartItemId, updateRequestDto);
    }

    @DeleteMapping("/items/{cartItemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public CartItemDto removeCartItemFromShoppingCart(
            Authentication authentication,
            @PathVariable Long cartItemId
    ) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService
                .removeCartItemFromShoppingCart(user.getId(), cartItemId);
    }
}
