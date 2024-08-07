package project.onlinebookstore.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.onlinebookstore.dto.order.CreateOrderRequestDto;
import project.onlinebookstore.dto.order.OrderDto;
import project.onlinebookstore.model.User;
import project.onlinebookstore.service.order.OrderService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public OrderDto createOrder(
            Authentication authentication,
            @RequestBody CreateOrderRequestDto createOrderRequestDto) {
        return orderService.createOrder(
                getAuthenticationUserByUserId(authentication),
                createOrderRequestDto);
    }

    private Long getAuthenticationUserByUserId(Authentication authentication) {
        return ((User) authentication.getPrincipal()).getId();
    }
}
