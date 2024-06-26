package project.onlinebookstore.service.impl;

import jakarta.persistence.EntityNotFoundException;
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
        Book bookModelForSave =
                bookRepository.save(bookMapper.toBookModel(bookRequestDto));
        return bookMapper.toBookDto(bookModelForSave);
    }

    @Override
    public List<BookDto> findAll() {
        List<Book> allBookFromRepository = bookRepository.findAll();
        return allBookFromRepository
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

    @Override
    public BookDto updateById(Long id, CreateBookRequestDto updateBookRequestDto) {
        Book bookByIdForUpdate = bookRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Can not find entity by id: " + id));
        bookByIdForUpdate.setId(id);
        bookByIdForUpdate.setTitle(updateBookRequestDto.getTitle());
        bookByIdForUpdate.setAuthor(updateBookRequestDto.getAuthor());
        bookByIdForUpdate.setIsbn(updateBookRequestDto.getIsbn());
        bookByIdForUpdate.setPrice(updateBookRequestDto.getPrice());
        bookByIdForUpdate.setDescription(updateBookRequestDto.getDescription());
        bookByIdForUpdate.setCoverImage(updateBookRequestDto.getCoverImage());

        return bookMapper.toBookDto(bookRepository.save(bookByIdForUpdate));
    }

}
