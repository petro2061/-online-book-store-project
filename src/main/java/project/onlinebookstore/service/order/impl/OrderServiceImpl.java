package project.onlinebookstore.service.order.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.onlinebookstore.dto.order.CreateOrderRequestDto;
import project.onlinebookstore.dto.order.OrderDto;
import project.onlinebookstore.exception.EntityNotFoundException;
import project.onlinebookstore.exception.OrderProcessingException;
import project.onlinebookstore.mapper.OrderMapper;
import project.onlinebookstore.model.Order;
import project.onlinebookstore.model.ShoppingCart;
import project.onlinebookstore.repository.order.OrderRepository;
import project.onlinebookstore.repository.shoppingcart.ShoppingCartRepository;
import project.onlinebookstore.service.order.OrderService;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public OrderDto createOrder(Long userId, CreateOrderRequestDto orderRequestDto) {

        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId).orElseThrow(
                () -> new EntityNotFoundException("Can't find user by Id: " + userId));

        if (shoppingCart.getCartItems().isEmpty()) {
            throw new OrderProcessingException("Cart items is empty for user: " + userId);
        }
        Order order = orderMapper.toModel(shoppingCart, orderRequestDto);

        order.getOrderItems().forEach(orderItem -> orderItem.setOrder(order));
        Order savedOrder = orderRepository.save(order);

        shoppingCart.getCartItems().clear();
        shoppingCartRepository.save(shoppingCart);

        return orderMapper.toDto(savedOrder);
    }
}
