package project.onlinebookstore.dto.cartitem;

import jakarta.validation.constraints.Positive;

public record CreateCartItemRequestDto(
        @Positive(message = "Field bookId can't be less one")
        Long bookId,
        @Positive(message = "Quantity can't be less one")
        int quantity
) {
}
