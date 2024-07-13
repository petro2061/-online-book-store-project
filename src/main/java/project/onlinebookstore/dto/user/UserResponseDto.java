package project.onlinebookstore.dto.user;

public record UserResponseDto(
        String id,
        String email,
        String firstName,
        String lastName,
        String shippingAddress
) {
}
