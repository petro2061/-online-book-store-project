package project.onlinebookstore.dto.order;

import jakarta.validation.constraints.NotBlank;

public record CreateOrderRequestDto(
        @NotBlank(message = "Shipping address can't be null")
        String shippingAddress
) {
}
