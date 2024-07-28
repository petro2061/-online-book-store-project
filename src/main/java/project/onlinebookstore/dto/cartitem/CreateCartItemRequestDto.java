package project.onlinebookstore.dto.cartitem;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CreateCartItemRequestDto(
        @NotNull (message = "Field bookId can't be null")
        Long bookId,
        @Min(1)
        int quantity
) {
}
