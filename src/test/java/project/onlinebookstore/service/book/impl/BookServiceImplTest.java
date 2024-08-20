package project.onlinebookstore.service.book.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import project.onlinebookstore.dto.book.BookDto;
import project.onlinebookstore.dto.book.CreateBookRequestDto;
import project.onlinebookstore.mapper.BookMapper;
import project.onlinebookstore.model.Book;
import project.onlinebookstore.repository.book.BookRepository;
import java.math.BigDecimal;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookServiceImpl bookServiceImpl;
    @Test
    @DisplayName("Check correct saving book into repository")
    void saveBook_withCreateBookRequestDto_validReturnBookDto() {
        //Given
        CreateBookRequestDto bookRequestDto = getCreateBookRequestDto();
        Book book = getBook(bookRequestDto);
        BookDto expectedBookDto = getBookDto(book);

        Mockito.when(bookMapper.toBookModel(bookRequestDto)).thenReturn(book);
        Mockito.when(bookRepository.save(book)).thenReturn(book);
        Mockito.when(bookMapper.toBookDto(book)).thenReturn(expectedBookDto);

        //When
        BookDto actualBookDto = bookServiceImpl.save(bookRequestDto);

        //Then
        Assertions.assertEquals(expectedBookDto, actualBookDto);
    }

    private CreateBookRequestDto getCreateBookRequestDto(){
        CreateBookRequestDto testCreateBookRequestDto = new CreateBookRequestDto();
        testCreateBookRequestDto.setTitle("The Clean Coder: A Code of Conduct for Professional Programmers");
        testCreateBookRequestDto.setAuthor("Robert C. Martin");
        testCreateBookRequestDto.setIsbn("978-0137081076");
        testCreateBookRequestDto.setPrice(BigDecimal.valueOf(40.31));
        testCreateBookRequestDto.setDescription("Yet another sample book description.");
        testCreateBookRequestDto.setCoverImage("https://example.com/cover1.jpg");
        testCreateBookRequestDto.setCategoriesIds(List.of());
        return testCreateBookRequestDto;

    }

    private Book getBook(CreateBookRequestDto bookRequestDto){
        Book book = new Book();
        book.setTitle(bookRequestDto.getTitle());
        book.setAuthor(bookRequestDto.getAuthor());
        book.setIsbn(bookRequestDto.getIsbn());
        book.setPrice(bookRequestDto.getPrice());
        book.setDescription(bookRequestDto.getDescription());
        book.setCoverImage(bookRequestDto.getCoverImage());
        return book;
    }

    private BookDto getBookDto(Book book){
        BookDto bookDto = new BookDto();
        bookDto.setTitle(book.getTitle());
        bookDto.setAuthor(book.getAuthor());
        bookDto.setIsbn(book.getIsbn());
        bookDto.setPrice(book.getPrice());
        bookDto.setDescription(book.getDescription());
        bookDto.setCoverImage(book.getTitle());
        return bookDto;
    }
}