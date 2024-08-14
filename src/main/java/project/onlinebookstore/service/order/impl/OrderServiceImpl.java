package project.onlinebookstore.service.order.impl;

import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.onlinebookstore.dto.order.CreateOrderRequestDto;
import project.onlinebookstore.dto.order.CreateOrderUpdateRequestDto;
import project.onlinebookstore.dto.order.OrderDto;
import project.onlinebookstore.dto.orderitem.OrderItemDto;
import project.onlinebookstore.exception.EntityNotFoundException;
import project.onlinebookstore.exception.OrderProcessingException;
import project.onlinebookstore.mapper.OrderItemMapper;
import project.onlinebookstore.mapper.OrderMapper;
import project.onlinebookstore.model.Order;
import project.onlinebookstore.model.OrderItem;
import project.onlinebookstore.model.ShoppingCart;
import project.onlinebookstore.repository.order.OrderRepository;
import project.onlinebookstore.repository.orderitem.OrderItemRepository;
import project.onlinebookstore.repository.shoppingcart.ShoppingCartRepository;
import project.onlinebookstore.service.order.OrderService;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

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

        orderRepository.save(order);

        shoppingCart.getCartItems().clear();
        shoppingCartRepository.save(shoppingCart);

        return orderMapper.toDto(order);
    }

    @Override
    public List<OrderDto> getAllOrders(Long userId, Pageable pageable) {
        List<Order> byUserId
                = orderRepository.findByUserId(userId, pageable);
        return byUserId.stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public OrderDto updateOrderStatus(
            Long orderId,
            CreateOrderUpdateRequestDto updateRequestDto
    ) {
        Order order = orderRepository.findByUserIdAndOrderId(orderId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Can't find order by orderId: "
                                + orderId));
        orderMapper.updateOrderStatus(updateRequestDto, order);
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public List<OrderItemDto> getAllOrderItemsByOrderId(Long userId, Long orderId) {
        return orderItemRepository.getAllOrderItemsByOrderId(userId, orderId).stream()
                .map(orderItemMapper::toDto)
                .toList();
    }

    @Override
    public OrderItemDto getOrderItemByOrderIdAndOrderItemId(
            Long userId,
            Long orderId,
            Long orderItemId) {
        OrderItem orderItem =
                orderItemRepository
                        .getOrderItemByOrderIdAndOrderItemId(userId, orderId, orderItemId)
                        .orElseThrow(() ->
                                new EntityNotFoundException("Can't find order item with Id: "
                                        + orderItemId
                                        + " by userId: "
                                        + userId
                                        + " and orderId: "
                                        + orderId));
        return orderItemMapper.toDto(orderItem);
    }
}
