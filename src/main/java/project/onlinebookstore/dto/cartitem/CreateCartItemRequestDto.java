package project.onlinebookstore.dto.cartitem;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateCartItemRequestDto(
        @NotNull(message = "Field bookId can't be null")
        Long bookId,
        @Positive(message = "Quantity can't be less one")
        int quantity
) {
}
