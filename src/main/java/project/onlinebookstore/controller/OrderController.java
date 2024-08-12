package project.onlinebookstore.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.RequiredArgsConstructor;
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

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public OrderDto createOrder(
            Authentication authentication,
            @RequestBody @Valid CreateOrderRequestDto createOrderRequestDto) {
        return orderService.createOrder(
                getAuthenticationUserByUserId(authentication),
                createOrderRequestDto);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public List<OrderDto> getAllOrders(
            Authentication authentication,
            @PageableDefault Pageable pageable) {
        return orderService.getAllOrder(
                getAuthenticationUserByUserId(authentication),
                pageable
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{orderId}")
    public OrderDto updateOrderStatus(
            @PathVariable @Positive Long orderId,
            @RequestBody @Valid CreateOrderUpdateRequestDto updateRequestDto) {
        return orderService.updateOrderStatus(
                orderId,
                updateRequestDto
        );
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{orderId}/items")
    public List<OrderItemDto> getAllOrderItems(
            Authentication authentication,
            @PathVariable @Positive Long orderId) {
        return orderService.getAllOrderItemsByOrderId(
                getAuthenticationUserByUserId(authentication),
                orderId
        );
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("{orderId}/items/{orderItemId}")
    public OrderItemDto getOrderItem(
            Authentication authentication,
            @PathVariable @Positive Long orderId,
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
