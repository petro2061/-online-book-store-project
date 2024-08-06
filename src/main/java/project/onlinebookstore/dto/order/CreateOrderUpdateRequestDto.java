package project.onlinebookstore.dto.order;

import project.onlinebookstore.model.Order;
import project.onlinebookstore.validation.ValidEnum;

public record CreateOrderUpdateRequestDto(
        @ValidEnum(enumClass = Order.Status.class, message = "Input incorrect order status")
        Order.Status status
) {
}
