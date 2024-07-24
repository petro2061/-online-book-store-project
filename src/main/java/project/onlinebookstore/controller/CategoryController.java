package project.onlinebookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import project.onlinebookstore.dto.book.BookDtoWithoutCategoryIds;
import project.onlinebookstore.dto.category.CategoryDto;
import project.onlinebookstore.dto.category.CreateCategoryRequestDto;
import project.onlinebookstore.service.book.BookService;
import project.onlinebookstore.service.category.CategoryService;

@Tag(name = "Category Management",
        description = "Contains category entity management operations")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final BookService bookService;

    @Operation(summary = "Create new category",
            description = "creates a new entity based on the input data")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public CategoryDto createCategory(
            @Parameter(description =
                    "Represents the data describing the entity of the category")
            @RequestBody @Valid CreateCategoryRequestDto categoryRequestDto
    ) {
        return categoryService.save(categoryRequestDto);
    }

    @Operation(summary = "Get all categories",
            description = "Allows get all categories using pagination")
    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public List<CategoryDto> getAll(
            @ParameterObject @PageableDefault Pageable pageable) {
        return categoryService.findAll(pageable);
    }

    @Operation(summary = "Get category by Id",
            description = "Allows you to find a category by its Id")
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}")
    public CategoryDto getCategoryById(
            @Parameter(description = "Represents the category identifier")
            @PathVariable @Positive Long id) {
        return categoryService.findById(id);
    }

    @Operation(summary = "Update category by Id",
            description = "Allows update category entity by Id")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public CategoryDto updateCategory(
            @Parameter(description = "Represents the category identifier")
            @PathVariable @Positive Long id,
            @Parameter(description = "Represents the data for update category in DB")
            @RequestBody @Valid CreateCategoryRequestDto categoryDto) {
        return categoryService.update(id, categoryDto);
    }

    @Operation(summary = "Delete category by Id")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteCategory(
            @Parameter(description = "Represents the category identifier")
            @PathVariable @Positive Long id) {
        categoryService.deleteById(id);
    }

    @Operation(summary = "Find all books by category Id",
            description = "Allows find all books by category Id and by certain parameters")
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}/books")
    public List<BookDtoWithoutCategoryIds> getBooksByCategoryId(
            @Parameter(description = "Represents the category identifier")
            @PathVariable @Positive Long id,
            @ParameterObject @PageableDefault Pageable pageable) {
        return bookService.findAllByCategoryId(id, pageable);
    }
}
