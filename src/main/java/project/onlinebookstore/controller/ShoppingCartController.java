package project.onlinebookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
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

@Tag(name = "Shopping Cart Management",
        description = "Contains shopping cart entity management operations")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @Operation(summary = "Get shopping cart",
            description = "Returns the shopping cart with all items for the authorized user")
    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public ShoppingCartDto getShoppingCart(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.getShoppingCart(user.getId());
    }

    @Operation(summary = "Add new product to shopping cart",
            description = "Allows get all new cart item to shopping cart")
    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public CartItemDto addCartItemToShoppingCart(
            Authentication authentication,
            @Parameter(description = "Represents the product data to be added to the cart")
            @RequestBody @Valid
            CreateCartItemRequestDto cartItemRequestDto) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService
                .addCartItemToShoppingCart(user.getId(), cartItemRequestDto);
    }

    @Operation(summary = "Update product in shopping cart",
            description = "Allows update product in the shopping cart by identifier")
    @PutMapping("/items/{cartItemId}")
    public CartItemDto updateCartItemInShoppingCart(
            Authentication authentication,
            @Parameter(description = "Represents the cart item identifier")
            @PathVariable @Positive Long cartItemId,
            @Parameter(description = "Represents the data for update product")
            @RequestBody @Valid
            CreateCartItemUpdateRequestDto updateRequestDto) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService
                .updateCartItemInShoppingCart(user.getId(), cartItemId, updateRequestDto);
    }

    @Operation(summary = "Delete product in shopping cart",
            description = "Allows delete product in the shopping cart by identifier")
    @DeleteMapping("/items/{cartItemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public CartItemDto removeCartItemFromShoppingCart(
            Authentication authentication,
            @Parameter(description = "Represents the cart item identifier")
            @PathVariable @Positive Long cartItemId
    ) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService
                .removeCartItemFromShoppingCart(user.getId(), cartItemId);
    }
}
