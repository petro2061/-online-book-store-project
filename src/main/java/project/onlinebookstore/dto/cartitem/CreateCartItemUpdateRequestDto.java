package project.onlinebookstore.dto.cartitem;

import jakarta.validation.constraints.Positive;

public record CreateCartItemUpdateRequestDto(
        @Positive(message = "Quantity can't be less one")
        int quantity
) {
}
