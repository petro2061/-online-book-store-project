package project.onlinebookstore.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.onlinebookstore.dto.BookDto;
import project.onlinebookstore.dto.CreateBookRequestDto;
import project.onlinebookstore.mapper.BookMapper;
import project.onlinebookstore.model.Book;
import project.onlinebookstore.repository.BookRepository;
import project.onlinebookstore.service.BookService;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public BookDto save(CreateBookRequestDto bookRequestDto) {
        Book book =
                bookRepository.save(bookMapper.toBookModel(bookRequestDto));
        return bookMapper.toBookDto(book);
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll()
                .stream()
                .map(bookMapper::toBookDto)
                .toList();
    }

    @Override
    public BookDto findById(Long id) {
        Book bookById = bookRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Can not find entity by id: " + id));
        return bookMapper.toBookDto(bookById);
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Transactional
    @Override
    public BookDto updateById(Long id, CreateBookRequestDto bookDto) {
        Book book = bookRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Can not find entity by id: " + id));
        bookMapper.updateBookFromDto(bookDto, book);
        return bookMapper.toBookDto(bookRepository.save(book));
    }

}
