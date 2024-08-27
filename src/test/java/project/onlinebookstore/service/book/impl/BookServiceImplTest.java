package project.onlinebookstore.service.book.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import project.onlinebookstore.dto.book.BookDto;
import project.onlinebookstore.dto.book.BookDtoWithoutCategoryIds;
import project.onlinebookstore.dto.book.BookSearchParameters;
import project.onlinebookstore.dto.book.CreateBookRequestDto;
import project.onlinebookstore.exception.EntityNotFoundException;
import project.onlinebookstore.mapper.BookMapper;
import project.onlinebookstore.model.Book;
import project.onlinebookstore.repository.book.BookRepository;
import project.onlinebookstore.repository.book.BookSpecificationBuilder;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private BookSpecificationBuilder bookSpecificationBuilder;

    @InjectMocks
    private BookServiceImpl bookServiceImpl;

    @Test
    @DisplayName("Check correct saving book into repository")
    void saveBook_withCreateBookRequestDto_getValidBookDto() {
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
        Assertions.assertEquals(actualBookDto, expectedBookDto);

        Mockito.verify(bookMapper).toBookModel(bookRequestDto);
        Mockito.verify(bookRepository).save(book);
        Mockito.verify(bookMapper).toBookDto(book);
        Mockito.verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("Check get all book with pageable param")
    void getAll_withPageable_shouldReturnAllBook() {
        //Given
        List<Book> bookList = getListBook();
        List<BookDto> bookDtoList = getListBookDto();
        Pageable pageable = PageRequest.of(0, 10);

        Mockito.when(bookRepository.findAll(pageable)).thenReturn(new PageImpl<>(bookList));
        Mockito.when(bookMapper.toBookDto(ArgumentMatchers.any(Book.class)))
                .thenReturn(bookDtoList.get(0));

        //When
        List<BookDto> actualListDto = bookServiceImpl.findAll(pageable);

        //Then
        Assertions.assertEquals(bookDtoList, actualListDto);

        Mockito.verify(bookRepository).findAll(pageable);
        Mockito.verify(bookMapper, Mockito.times(bookDtoList.size())).toBookDto(bookList.get(0));
        Mockito.verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("Find book by correct id ")
    void getBookById_withValidId_shouldReturnValidBook() {
        //Given
        Long bookId = 1L;
        Book book = getBook(getCreateBookRequestDto());
        BookDto exspectedBookDto = getBookDto(book);

        Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        Mockito.when((bookMapper.toBookDto(book))).thenReturn(exspectedBookDto);

        //When
        BookDto actual = bookServiceImpl.findById(1L);

        //Then
        Assertions.assertEquals(exspectedBookDto, actual);

        Mockito.verify(bookRepository).findById(bookId);
        Mockito.verify(bookMapper).toBookDto(book);
        Mockito.verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("Find book with not valid id")
    void getBookById_withNotValidId_shouldGetThrowException() {
        //Given
        Long notValidBookId = 200L;

        Mockito.when(bookRepository.findById(notValidBookId))
                .thenReturn(Optional.empty());

        //When
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> bookServiceImpl.findById(notValidBookId));

        Mockito.verify(bookRepository).findById(notValidBookId);
        Mockito.verify(bookMapper, Mockito.never()).toBookDto(ArgumentMatchers.any());
        Mockito.verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("Delete book by valid id")
    void deleteBookById_withValidId_shouldDeleteBook() {
        //Given
        Long deleteBookId = 1L;

        //When
        bookServiceImpl.deleteById(deleteBookId);

        Mockito.verify(bookRepository).deleteById(deleteBookId);
        Mockito.verifyNoMoreInteractions(bookRepository);
    }

    @Test
    @DisplayName("Update book by id")
    void getUpdateBook_withValidId_returnUpdateBookDto() {
        //Given
        CreateBookRequestDto bookRequestDto = getUpdateCreateRequestBookDto();
        Book book = getBook(getCreateBookRequestDto());
        Book updateBook = getUpdateBook(bookRequestDto);
        BookDto updateBookDto = getUpdateBookDto(updateBook);
        Long updateBookId = 1L;

        Mockito.when(bookRepository.findById(updateBookId)).thenReturn(Optional.of(book));
        Mockito.doNothing().when(bookMapper).updateBookFromDto(bookRequestDto, book);
        Mockito.when(bookRepository.save(book)).thenReturn(updateBook);
        Mockito.when(bookMapper.toBookDto(updateBook)).thenReturn(updateBookDto);

        BookDto bookDto = bookServiceImpl.updateById(updateBookId, bookRequestDto);

        Assertions.assertEquals(updateBookDto, bookDto);

        Mockito.verify(bookRepository).findById(updateBookId);
        Mockito.verify(bookMapper).updateBookFromDto(bookRequestDto, book);
        Mockito.verify(bookRepository).save(book);
        Mockito.verify(bookMapper).toBookDto(updateBook);
        Mockito.verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("Update book with not valid id")
    void getUpdateBook_withNotValidId_shouldGetException() {
        //Given
        CreateBookRequestDto bookRequestDto = getUpdateCreateRequestBookDto();
        Long notValidUpdateBookId = 1000L;

        Mockito.when(bookRepository.findById(notValidUpdateBookId))
                .thenReturn(Optional.empty());

        //When
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> bookServiceImpl.updateById(notValidUpdateBookId, bookRequestDto));

        Mockito.verify(bookRepository).findById(notValidUpdateBookId);
        Mockito.verify(bookMapper, Mockito.never())
                .updateBookFromDto(ArgumentMatchers.any(), ArgumentMatchers.any());
        Mockito.verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("Returns books based on specific search parameters")
    void getSearchBooks_withSearchParameters_returnAllBooksForSearchParameters() {
        //Given
        BookSearchParameters bookSearchParameters = new BookSearchParameters(
                        new String[]{"Sample Book 1"},
                        new String[]{},
                        new String[]{},
                        new BigDecimal[]{},
                        new Long[]{}
        );
        Pageable pageable = PageRequest.of(0, 10);
        List<Book> bookList = getListBook();
        List<BookDto> bookDtoList = getListBookDto();
        Specification<Book> specification = bookSpecificationBuilder.build(bookSearchParameters);

        Mockito.when(bookSpecificationBuilder.build(bookSearchParameters))
                .thenReturn(specification);
        Mockito.when(bookRepository.findAll(specification, pageable))
                .thenReturn(new PageImpl<>(bookList));
        Mockito.when(bookMapper.toBookDto(ArgumentMatchers.any(Book.class)))
                .thenReturn(bookDtoList.get(0));

        //When
        List<BookDto> actualBookDtoList =
                bookServiceImpl.searchBooks(bookSearchParameters, pageable);

        //Then
        Assertions.assertEquals(bookDtoList, actualBookDtoList);

        Mockito.verify(bookRepository).findAll(specification, pageable);
        Mockito.verify(bookMapper, Mockito.times(bookList.size()))
                .toBookDto(ArgumentMatchers.any(Book.class));
        Mockito.verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("Returns the books of a given category by category identifier")
    void getAllBookByCategoryId_withValidCategoryId_returnAllBookForCategoryId() {
        //Given
        Long categoryId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        List<Book> bookList = getListBook();
        List<BookDtoWithoutCategoryIds> bookDtoWithoutCategoryIdsList =
                getBookDtoWithoutCategoryIds();

        Mockito.when(bookRepository.findAllByCategoryId(categoryId, pageable)).thenReturn(bookList);
        Mockito.when(bookMapper.toDtoWithoutCategories(ArgumentMatchers.any(Book.class)))
                .thenReturn(bookDtoWithoutCategoryIdsList.get(0));

        //When
        List<BookDtoWithoutCategoryIds> actualAllBookListByCategoryId =
                bookServiceImpl.findAllByCategoryId(categoryId, pageable);

        //Then
        Assertions.assertEquals(bookDtoWithoutCategoryIdsList, actualAllBookListByCategoryId);

        Mockito.verify(bookRepository).findAllByCategoryId(categoryId, pageable);
        Mockito.verify(bookMapper, Mockito.times(bookList.size()))
                .toDtoWithoutCategories(ArgumentMatchers.any(Book.class));
        Mockito.verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    private CreateBookRequestDto getCreateBookRequestDto() {
        CreateBookRequestDto createBookRequestDto = new CreateBookRequestDto();
        createBookRequestDto.setTitle("Sample Book");
        createBookRequestDto.setAuthor("Author");
        createBookRequestDto.setIsbn("9781234567897");
        createBookRequestDto.setPrice(BigDecimal.valueOf(40.31));
        createBookRequestDto.setDescription("This is a sample book description.");
        createBookRequestDto.setCoverImage("https://example.com/cover1.jpg");
        createBookRequestDto.setCategoriesIds(List.of());
        return createBookRequestDto;

    }

    private Book getBook(CreateBookRequestDto bookRequestDto) {
        Book book = new Book();
        book.setTitle(bookRequestDto.getTitle());
        book.setAuthor(bookRequestDto.getAuthor());
        book.setIsbn(bookRequestDto.getIsbn());
        book.setPrice(bookRequestDto.getPrice());
        book.setDescription(bookRequestDto.getDescription());
        book.setCoverImage(bookRequestDto.getCoverImage());
        return book;
    }

    private BookDto getBookDto(Book book) {
        BookDto bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setTitle(book.getTitle());
        bookDto.setAuthor(book.getAuthor());
        bookDto.setIsbn(book.getIsbn());
        bookDto.setPrice(book.getPrice());
        bookDto.setDescription(book.getDescription());
        bookDto.setCoverImage(book.getTitle());
        return bookDto;
    }

    private List<Book> getListBook() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Sample Book 1");
        book.setAuthor("Author A");
        book.setIsbn("9781234567897");
        book.setPrice(BigDecimal.valueOf(19.99));
        book.setDescription("This is a sample book description.");
        book.setCoverImage("http://example.com/cover1.jpg");
        book.setCategories(Set.of());

        return List.of(book);
    }

    private List<BookDto> getListBookDto() {
        BookDto bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setTitle("Sample Book 1");
        bookDto.setAuthor("Author A");
        bookDto.setIsbn("9781234567897");
        bookDto.setPrice(BigDecimal.valueOf(19.99));
        bookDto.setDescription("This is a sample book description.");
        bookDto.setCoverImage("http://example.com/cover1.jpg");
        bookDto.setCategoriesIds(List.of());

        return List.of(bookDto);
    }

    CreateBookRequestDto getUpdateCreateRequestBookDto() {
        CreateBookRequestDto updateBookRequestDto = getCreateBookRequestDto();
        updateBookRequestDto.setTitle("Sample Book 3");
        updateBookRequestDto.setAuthor("Author C");
        updateBookRequestDto.setPrice(BigDecimal.valueOf(100.5));
        return updateBookRequestDto;
    }

    private Book getUpdateBook(CreateBookRequestDto updateBookRequestDto) {
        Book updateBook = getBook(getCreateBookRequestDto());
        updateBook.setTitle(updateBookRequestDto.getTitle());
        updateBook.setAuthor(updateBookRequestDto.getAuthor());
        updateBook.setPrice(updateBookRequestDto.getPrice());

        return updateBook;
    }

    private BookDto getUpdateBookDto(Book updateBook) {
        BookDto updateBookDto = new BookDto();
        updateBookDto.setId(1L);
        updateBookDto.setTitle(updateBook.getTitle());
        updateBookDto.setAuthor(updateBook.getAuthor());
        updateBookDto.setIsbn(updateBook.getIsbn());
        updateBookDto.setPrice(updateBook.getPrice());
        updateBookDto.setDescription(updateBook.getDescription());
        updateBookDto.setCoverImage(updateBook.getTitle());

        return updateBookDto;
    }

    private List<BookDtoWithoutCategoryIds> getBookDtoWithoutCategoryIds() {
        BookDtoWithoutCategoryIds bookDtoWithoutCategoryIds = new BookDtoWithoutCategoryIds();
        bookDtoWithoutCategoryIds.setId(1L);
        bookDtoWithoutCategoryIds.setTitle("Sample Book 1");
        bookDtoWithoutCategoryIds.setAuthor("Author A");
        bookDtoWithoutCategoryIds.setIsbn("9781234567897");
        bookDtoWithoutCategoryIds.setPrice(BigDecimal.valueOf(19.99));
        bookDtoWithoutCategoryIds.setDescription("This is a sample book description.");
        bookDtoWithoutCategoryIds.setCoverImage("http://example.com/cover1.jpg");

        return List.of(bookDtoWithoutCategoryIds);
    }
}
