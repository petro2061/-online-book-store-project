package project.onlinebookstore.dto.order;

import jakarta.validation.constraints.NotBlank;

public record CreateOrderRequestDto(
        @NotBlank(message = "Sipping address can't be null")
        String shippingAddress
) {
}
