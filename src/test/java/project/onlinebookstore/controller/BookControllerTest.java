package project.onlinebookstore.controller;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.assertj.core.util.DoubleComparator;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import project.onlinebookstore.dto.book.BookDto;
import project.onlinebookstore.dto.book.CreateBookRequestDto;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerTest {

    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired DataSource dataSource,
            @Autowired WebApplicationContext applicationContext
    ) throws SQLException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
        teardown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/insert-into-categories.sql"));
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/insert-into-books.sql"));
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/insert-into-books_categories.sql"));
        }
    }

    @AfterAll
    static void afterAll(
            @Autowired DataSource dataSource
    ) {
        teardown(dataSource);
    }

    @SneakyThrows
    static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/delete-all-from-books_categories.sql"));
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/delete-all-from-books.sql"));
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/delete-all-from-categories.sql"));
        }
    }

    @WithMockUser(username = "user")
    @Test
    @Order(1)
    @DisplayName("Return all books from database")
    void getAllBooks_SavedBooks_AllBooksFromDatabase() throws Exception {
        //Given
        List<BookDto> expectedListBookDto = getListBookDto();

        //When
        MvcResult result = mockMvc.perform(get("/books")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        //Then
        byte[] jsonResponse = result.getResponse().getContentAsByteArray();
        BookDto[] bookDtoResponseArray = objectMapper.readValue(jsonResponse, BookDto[].class);
        List<BookDto> actualBookDtoList = Arrays.stream(bookDtoResponseArray).toList();

        Assertions.assertFalse(actualBookDtoList.isEmpty());
        Assertions.assertEquals(2L, actualBookDtoList.size());
        assertThat(actualBookDtoList)
                .usingRecursiveComparison()
                .withComparatorForType(new DoubleComparator(0.001), Double.class)
                .ignoringFields("id")
                .isEqualTo(expectedListBookDto);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Order(2)
    @DisplayName("Test book creation whit valid BookDto")
    void getCreateBook_whitValidCreateBookDto_ReturnsSavedBookDto() throws Exception {
        // Given
        CreateBookRequestDto bookRequestDto = getBookRequestDto();
        BookDto expectedBookDto = getBookDtoFromRequestDto(bookRequestDto);
        String jsonRequest = objectMapper.writeValueAsString(bookRequestDto);

        // When
        MvcResult result = mockMvc.perform(post("/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        String jsonResponse = result.getResponse().getContentAsString();
        BookDto actualBookDto = objectMapper.readValue(jsonResponse, BookDto.class);

        Assertions.assertNotNull(actualBookDto);
        Assertions.assertNotNull(actualBookDto.getId());
        Assertions.assertTrue(EqualsBuilder.reflectionEquals(expectedBookDto, actualBookDto, "id"));
    }

    @WithMockUser(username = "user")
    @Test
    @Order(3)
    @DisplayName("Return BookDto from database with valid id")
    void getBookById_withValidBookId_returnsValidBookDto() throws Exception {
        //Given
        BookDto expectedBookDto = getListBookDto().get(0);
        Long bookId = 1L;

        //When
        MvcResult result = mockMvc.perform(get("/books/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        //Then
        String jsonResponse = result.getResponse().getContentAsString();
        BookDto actualBookDto = objectMapper.readValue(jsonResponse, BookDto.class);

        Assertions.assertNotNull(actualBookDto);
        Assertions.assertTrue(EqualsBuilder.reflectionEquals(expectedBookDto, actualBookDto, "id"));
    }

    @WithMockUser(username = "user")
    @Test
    @Order(4)
    @DisplayName("Search books in database by search parameters")
    void searchBooks_withValidSearchParameters_returnsSearchBookDtoList() throws Exception {
        //Given
        String[] titles = {"Murder on the Orient Express"};
        BigDecimal[] prices = {BigDecimal.valueOf(12.990), BigDecimal.valueOf(25.000)};
        BookDto expectedSearchBookdto = getListBookDto().get(1);

        //When
        MvcResult result = mockMvc.perform(get("/books/search")
                        .param("titles", titles)
                        .param("prices", prices[0].toPlainString(), prices[1].toPlainString())
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        //Then
        byte[] jsonResponse = result.getResponse().getContentAsByteArray();
        BookDto[] bookDtoResponseArray = objectMapper.readValue(jsonResponse, BookDto[].class);
        List<BookDto> actualBookDtoList = Arrays.stream(bookDtoResponseArray).toList();

        Assertions.assertNotNull(actualBookDtoList);
        Assertions.assertTrue(EqualsBuilder
                .reflectionEquals(expectedSearchBookdto, actualBookDtoList.get(0), "id"));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Order(5)
    @DisplayName("Update book in database")
    void updateBookById_withValidBookId_returnsUpdateBookDto() throws Exception {
        //Given
        Long updateBookId = 2L;
        CreateBookRequestDto updateBookRequestDto = getUpdateBookRequestDto();
        BookDto expectedBookDto = getBookDtoFromRequestDto(updateBookRequestDto);
        String jsonRequest = objectMapper.writeValueAsString(updateBookRequestDto);

        //When
        MvcResult result = mockMvc.perform(put("/books/{id}", updateBookId)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        //Then
        String jsonResponse = result.getResponse().getContentAsString();
        BookDto actualBookDto = objectMapper.readValue(jsonResponse, BookDto.class);

        Assertions.assertNotNull(actualBookDto);
        Assertions.assertTrue(EqualsBuilder.reflectionEquals(expectedBookDto, actualBookDto, "id"));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Order(6)
    @DisplayName("Delete book from database by id")
    void deleteById_withValidBookId_notReturnsData() throws Exception {
        //Given
        Long bookId = 2L;

        //When
        mockMvc.perform(delete("/books/{id}", bookId))
                .andExpect(status().isNoContent());
    }

    private CreateBookRequestDto getBookRequestDto() {
        CreateBookRequestDto bookRequestDto = new CreateBookRequestDto();
        bookRequestDto.setTitle("Sample title 1");
        bookRequestDto.setAuthor("Sample author 1");
        bookRequestDto.setIsbn("978-617-12-3364-5");
        bookRequestDto.setPrice(BigDecimal.valueOf(19.900));
        bookRequestDto.setDescription("This is a sample book description.");
        bookRequestDto.setCoverImage("http://example.com/cover.jpg");
        bookRequestDto.setCategoriesIds(List.of(1L));
        return bookRequestDto;
    }

    private CreateBookRequestDto getUpdateBookRequestDto() {
        CreateBookRequestDto updateBookRequestDto = new CreateBookRequestDto();
        updateBookRequestDto.setTitle("Sample title update");
        updateBookRequestDto.setAuthor("Sample author update");
        updateBookRequestDto.setIsbn("978-617-12-3364-9");
        updateBookRequestDto.setPrice(BigDecimal.valueOf(20.990));
        updateBookRequestDto.setDescription("This is a sample book description.");
        updateBookRequestDto.setCoverImage("http://example.com/cover.jpg");
        updateBookRequestDto.setCategoriesIds(List.of(1L));
        return updateBookRequestDto;
    }

    private BookDto getBookDtoFromRequestDto(CreateBookRequestDto bookRequestDto) {
        BookDto bookDto = new BookDto();
        bookDto.setTitle(bookRequestDto.getTitle());
        bookDto.setAuthor(bookRequestDto.getAuthor());
        bookDto.setIsbn(bookRequestDto.getIsbn());
        bookDto.setPrice(bookRequestDto.getPrice());
        bookDto.setDescription(bookRequestDto.getDescription());
        bookDto.setCoverImage(bookRequestDto.getCoverImage());
        bookDto.setCategoriesIds(bookRequestDto.getCategoriesIds());
        return bookDto;
    }

    private List<BookDto> getListBookDto() {
        BookDto firstBookDto = new BookDto();
        firstBookDto.setTitle("Robots and Empire");
        firstBookDto.setAuthor("Isaac Asimov");
        firstBookDto.setIsbn("9780008277796");
        firstBookDto.setPrice(BigDecimal.valueOf(360.000)
                .setScale(3, RoundingMode.HALF_UP));
        firstBookDto.setDescription("This is a sample book description. 1");
        firstBookDto.setCoverImage("http://example.com/cover1.jpg");
        firstBookDto.setCategoriesIds(List.of(1L));

        BookDto secondBookDto = new BookDto();
        secondBookDto.setTitle("Murder on the Orient Express");
        secondBookDto.setAuthor("Agatha Christie");
        secondBookDto.setIsbn("978-0063160354");
        secondBookDto.setPrice(BigDecimal.valueOf(13.990)
                .setScale(3, RoundingMode.HALF_UP));
        secondBookDto.setDescription("This is a sample book description. 2");
        secondBookDto.setCoverImage("http://example.com/cover2.jpg");
        secondBookDto.setCategoriesIds(List.of(2L));

        return List.of(firstBookDto, secondBookDto);
    }
}
