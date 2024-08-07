package project.onlinebookstore.service.order;

import project.onlinebookstore.dto.order.CreateOrderRequestDto;
import project.onlinebookstore.dto.order.OrderDto;

public interface OrderService {
    OrderDto createOrder(Long userId, CreateOrderRequestDto orderRequestDto);
}
