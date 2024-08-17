package project.onlinebookstore.service.order;

import java.util.List;
import org.springframework.data.domain.Pageable;
import project.onlinebookstore.dto.order.CreateOrderRequestDto;
import project.onlinebookstore.dto.order.CreateOrderUpdateRequestDto;
import project.onlinebookstore.dto.order.OrderDto;
import project.onlinebookstore.dto.orderitem.OrderItemDto;

public interface OrderService {
    OrderDto createOrder(Long userId, CreateOrderRequestDto orderRequestDto);

    List<OrderDto> getAllOrders(Long userId, Pageable pageable);

    OrderDto updateOrderStatus(
            Long orderId,
            CreateOrderUpdateRequestDto updateRequestDto);

    List<OrderItemDto> getAllOrderItemsByOrderId(Long userId, Long orderId);

    OrderItemDto getOrderItemByOrderIdAndOrderItemId(
            Long userId,
            Long orderId,
            Long orderItemId);
}
