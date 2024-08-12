package project.onlinebookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.onlinebookstore.dto.order.CreateOrderRequestDto;
import project.onlinebookstore.dto.order.CreateOrderUpdateRequestDto;
import project.onlinebookstore.dto.order.OrderDto;
import project.onlinebookstore.dto.orderitem.OrderItemDto;
import project.onlinebookstore.model.User;
import project.onlinebookstore.service.order.OrderService;

@Tag(name = "Order Management",
        description = "Contains order entity management operations ")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @Operation(summary = "Create new order",
            description = "Returns a new order for an authorized "
                    + "user based on the user's cart")
    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public OrderDto createOrder(
            Authentication authentication,
            @Parameter(description = "Represents the customer's shipping address")
            @RequestBody @Valid CreateOrderRequestDto createOrderRequestDto) {
        return orderService.createOrder(
                getAuthenticationUserByUserId(authentication),
                createOrderRequestDto);
    }

    @Operation(summary = "Get all orders",
            description = "Returns all orders for an authorized user")
    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public List<OrderDto> getAllOrders(
            Authentication authentication,
            @ParameterObject @PageableDefault Pageable pageable) {
        return orderService.getAllOrder(
                getAuthenticationUserByUserId(authentication),
                pageable
        );
    }

    @Operation(summary = "Update order",
            description = "Updates order status by order identifier")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{orderId}")
    public OrderDto updateOrderStatus(
            @Parameter(description = "Represents the order identifier")
            @PathVariable @Positive Long orderId,
            @Parameter(description = "Represents order status")
            @RequestBody @Valid CreateOrderUpdateRequestDto updateRequestDto) {
        return orderService.updateOrderStatus(
                orderId,
                updateRequestDto
        );
    }

    @Operation(summary = "Get all order items",
            description = "Returns all order items by order identifier")
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{orderId}/items")
    public List<OrderItemDto> getAllOrderItems(
            Authentication authentication,
            @Parameter(description = "Represents the order identifier")
            @PathVariable @Positive Long orderId) {
        return orderService.getAllOrderItemsByOrderId(
                getAuthenticationUserByUserId(authentication),
                orderId
        );
    }

    @Operation(summary = "Get order item",
            description = "Returns order items by order identifier and order item identifier")
    @PreAuthorize("hasRole('USER')")
    @GetMapping("{orderId}/items/{orderItemId}")
    public OrderItemDto getOrderItem(
            Authentication authentication,
            @Parameter(description = "Represents the order identifier")
            @PathVariable @Positive Long orderId,
            @Parameter(description = "Represents the order item identifier")
            @PathVariable @Positive Long orderItemId) {
        return orderService.getOrderItemByOrderIdAndOrderItemId(
                getAuthenticationUserByUserId(authentication),
                orderId,
                orderItemId
        );
    }

    private Long getAuthenticationUserByUserId(Authentication authentication) {
        return ((User) authentication.getPrincipal()).getId();
    }
}
