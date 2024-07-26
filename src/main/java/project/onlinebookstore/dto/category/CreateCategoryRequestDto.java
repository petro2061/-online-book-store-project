package project.onlinebookstore.dto.category;

import jakarta.validation.constraints.NotBlank;

public record CreateCategoryRequestDto(
        @NotBlank (message = "Field name can't be null")
        String name,
        String description
) {
}
