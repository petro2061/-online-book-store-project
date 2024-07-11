package project.onlinebookstore.service.book;

import java.util.List;
import org.springframework.data.domain.Pageable;
import project.onlinebookstore.dto.book.BookDto;
import project.onlinebookstore.dto.book.BookSearchParameters;
import project.onlinebookstore.dto.book.CreateBookRequestDto;

public interface BookService {
    BookDto save(CreateBookRequestDto bookRequestDto);

    List<BookDto> findAll(Pageable pageable);

    BookDto findById(Long id);

    void deleteById(Long id);

    BookDto updateById(Long id, CreateBookRequestDto updateBookRequestDto);

    List<BookDto> searchBooks(BookSearchParameters searchParameters, Pageable pageable);
}
