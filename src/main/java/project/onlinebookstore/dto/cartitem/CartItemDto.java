package project.onlinebookstore.dto.cartitem;

public record CartItemDto(
        Long id,
        Long bookId,
        String bookTitle,
        int quantity
) {
}
