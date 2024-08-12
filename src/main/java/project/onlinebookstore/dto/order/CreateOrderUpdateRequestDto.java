package project.onlinebookstore.dto.order;

import jakarta.validation.constraints.NotNull;
import project.onlinebookstore.model.Order;

@NotNull(message = "Status cannot be null")
public record CreateOrderUpdateRequestDto(
        Order.Status status
) {
}
