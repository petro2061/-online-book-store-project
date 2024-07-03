package project.onlinebookstore.service;

import java.util.List;
import org.springframework.data.domain.Pageable;
import project.onlinebookstore.dto.BookDto;
import project.onlinebookstore.dto.BookSearchParameters;
import project.onlinebookstore.dto.CreateBookRequestDto;

public interface BookService {
    BookDto save(CreateBookRequestDto bookRequestDto);

    List<BookDto> findAll(Pageable pageable);

    BookDto findById(Long id);

    void deleteById(Long id);

    BookDto updateById(Long id, CreateBookRequestDto updateBookRequestDto);

    List<BookDto> searchBooks(BookSearchParameters searchParameters, Pageable pageable);
}
