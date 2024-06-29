package project.onlinebookstore.service;

import java.util.List;
import project.onlinebookstore.dto.BookDto;
import project.onlinebookstore.dto.CreateBookRequestDto;

public interface BookService {
    BookDto save(CreateBookRequestDto bookRequestDto);

    List<BookDto> findAll();

    BookDto findById(Long id);

    void deleteById(Long id);

    BookDto updateById(Long id, CreateBookRequestDto updateBookRequestDto);
}
