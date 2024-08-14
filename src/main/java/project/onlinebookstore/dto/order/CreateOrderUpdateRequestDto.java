package project.onlinebookstore.dto.order;

import jakarta.validation.constraints.NotNull;
import project.onlinebookstore.model.Order;

public record CreateOrderUpdateRequestDto(
        @NotNull(message = "Status cannot be null")
        Order.Status status
) {
}
