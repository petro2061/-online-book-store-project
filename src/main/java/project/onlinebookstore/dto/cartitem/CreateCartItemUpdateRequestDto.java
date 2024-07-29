package project.onlinebookstore.dto.cartitem;

import jakarta.validation.constraints.Min;

public record CreateCartItemUpdateRequestDto(
        @Min(value = 1, message = "Quantity can't be less one")
        int quantity
) {
}
