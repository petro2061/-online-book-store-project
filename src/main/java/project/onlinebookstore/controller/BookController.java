package project.onlinebookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import project.onlinebookstore.dto.BookDto;
import project.onlinebookstore.dto.BookSearchParameters;
import project.onlinebookstore.dto.CreateBookRequestDto;
import project.onlinebookstore.service.BookService;

@Tag(name = "Book Management",
        description = "Contains book entity management operations")
@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @Operation(summary = "Get all books",
            description = "Allows get all books using pagination")
    @GetMapping
    public List<BookDto> getAll(
            @Parameter(description = "Standard object pageable "
                    + "include page, size and sort")
            Pageable pageable) {
        return bookService.findAll(pageable);
    }

    @Operation(summary = "Get book by Id",
            description = "Allows get a book by Id")
    @GetMapping("/{id}")
    public BookDto getBookById(
            @Parameter(description = "Represents the book identifier")
            @PathVariable Long id) {
        return bookService.findById(id);
    }

    @Operation(summary = "Delete book by Id",
            description = "Allows delete a book by Id")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(
            @Parameter(description = "Represents the book identifier")
            @PathVariable Long id) {
        bookService.deleteById(id);
    }

    @Operation(summary = "Create new book",
            description = "creates a new entity based on the input data")
    @PostMapping
    public BookDto createBook(
            @Parameter(description =
                    "Represents the data describing the entity of the book")
            @RequestBody @Valid CreateBookRequestDto bookDto) {
        return bookService.save(bookDto);
    }

    @Operation(summary = "Update book by Id",
            description = "Allows update book by identifier")
    @PutMapping("/{id}")
    public BookDto updateBookById(
            @Parameter(description = "Represents the book identifier")
            @PathVariable Long id,
            @Parameter(description = "Represents the data for update book in DB")
            @RequestBody @Valid CreateBookRequestDto updateBookDto) {
        return bookService.updateById(id, updateBookDto);
    }

    @Operation(summary = "Search book by parameters",
            description = "Allows searching for books by certain parameters")
    @GetMapping("/search")
    public List<BookDto> searchBooks(
            @Parameter(description = "Represents an object whose fields represent"
                    + " an array of search parameters")
            BookSearchParameters searchParameters) {
        return bookService.searchBooks(searchParameters);
    }
}
