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
import project.onlinebookstore.dto.book.BookDtoWithoutCategoryIds;
import project.onlinebookstore.dto.category.CategoryDto;
import project.onlinebookstore.dto.category.CreateCategoryRequestDto;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CategoryControllerTest {

    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired DataSource dataSource,
            @Autowired WebApplicationContext webApplicationContext)
            throws SQLException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity()).build();
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
    @DisplayName("Returns all categories from database")
    void getAllCategories_SavedCategory_AllCategoriesFromDatabase() throws Exception {
        //Given
        List<CategoryDto> expectedListCategoryDto = getListCategoryDto();

        //When
        MvcResult result = mockMvc.perform(get("/categories")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        //Then
        byte[] jsonResponse = result.getResponse().getContentAsByteArray();
        CategoryDto[] categoryDtoResponseArray =
                objectMapper.readValue(jsonResponse, CategoryDto[].class);
        List<CategoryDto> actualListCategoryDto = Arrays.stream(categoryDtoResponseArray).toList();

        Assertions.assertFalse(actualListCategoryDto.isEmpty());
        Assertions.assertEquals(2L, actualListCategoryDto.size());
        assertThat(actualListCategoryDto)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expectedListCategoryDto);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Order(2)
    @DisplayName("Create category with valid CategoryDto in database")
    void createCategory_withValidCategoryRequestDto_returnsSavedCategoryDto() throws Exception {
        //Given
        CreateCategoryRequestDto categoryRequestDto = getCategoryRequestDto();
        CategoryDto expectedCategoryDto = getCategoryDtoFromRequestDto(categoryRequestDto);
        String jsonRequest = objectMapper.writeValueAsString(categoryRequestDto);

        //Then
        MvcResult result = mockMvc.perform(post("/categories")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        //Then
        String jsonResponse = result.getResponse().getContentAsString();
        CategoryDto actualCategoryDto = objectMapper.readValue(jsonResponse, CategoryDto.class);

        Assertions.assertNotNull(actualCategoryDto);
        Assertions.assertNotNull(actualCategoryDto.id());
        Assertions.assertTrue(EqualsBuilder
                .reflectionEquals(expectedCategoryDto, actualCategoryDto, "id"));
    }

    @WithMockUser(username = "user")
    @Test
    @Order(3)
    @DisplayName("Get CategoryDto from database by valid id")
    void getCategoryById_withValidCategoryId_returnsValidCategoryDto() throws Exception {
        //Given
        Long categoryId = 1L;
        CategoryDto expectedCategoryDto = getListCategoryDto().get(0);

        //When
        MvcResult result = mockMvc.perform(get("/categories/{id}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        //Then
        String jsonResponse = result.getResponse().getContentAsString();
        CategoryDto actualCategoryDto = objectMapper.readValue(jsonResponse, CategoryDto.class);

        Assertions.assertNotNull(actualCategoryDto);
        Assertions.assertTrue(EqualsBuilder
                .reflectionEquals(expectedCategoryDto, actualCategoryDto, "id"));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Order(4)
    @DisplayName("Update category by id")
    void updateCategory_byValidCategoryId_returnsUpdateCategoryDto() throws Exception {
        //Given
        Long updateCategoryId = 3L;
        CreateCategoryRequestDto updateCategoryRequestDto = getUpdateCategoryRequestDto();
        CategoryDto expectedCategoryDto = getCategoryDtoFromRequestDto(updateCategoryRequestDto);
        String jsonRequest = objectMapper.writeValueAsString(updateCategoryRequestDto);

        //When
        MvcResult result = mockMvc.perform(put("/categories/{id}", updateCategoryId)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        //Then
        String jsonResponse = result.getResponse().getContentAsString();
        CategoryDto actualCategoryDto = objectMapper.readValue(jsonResponse, CategoryDto.class);

        Assertions.assertNotNull(actualCategoryDto);
        Assertions.assertTrue(EqualsBuilder
                .reflectionEquals(expectedCategoryDto, actualCategoryDto, "id"));
    }

    @WithMockUser(username = "user")
    @Test
    @Order(5)
    @DisplayName("Get list all books by category id")
    void getAllBooksByCategoryId_withValidCategoryId_returnsListBookWithoutCategory()
            throws Exception {
        //Given
        Long categoryId = 1L;
        List<BookDtoWithoutCategoryIds> expectedBookDtoWithoutCategoryIds =
                getListBookDtoWithoutCategoryIds();

        //When
        MvcResult result = mockMvc.perform(get("/categories/{id}/books", categoryId)
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        //Then
        byte[] jsonResponse = result.getResponse().getContentAsByteArray();
        BookDtoWithoutCategoryIds[] bookDtoWithoutCategoryIdsArray =
                objectMapper.readValue(jsonResponse, BookDtoWithoutCategoryIds[].class);
        List<BookDtoWithoutCategoryIds> actualBookDtoWithoutCategoryIds =
                Arrays.stream(bookDtoWithoutCategoryIdsArray).toList();

        Assertions.assertFalse(actualBookDtoWithoutCategoryIds.isEmpty());
        Assertions.assertEquals(1L, actualBookDtoWithoutCategoryIds.size());
        assertThat(actualBookDtoWithoutCategoryIds)
                .usingRecursiveComparison()
                .withComparatorForType(new DoubleComparator(0.001), Double.class)
                .ignoringFields("id")
                .isEqualTo(expectedBookDtoWithoutCategoryIds);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Order(6)
    @DisplayName("Delete Category by valid id")
    void deleteCategory_withValidCategoryId_notReturnsData() throws Exception {
        //Given
        Long deleteCategoryId = 3L;

        //When
        mockMvc.perform(delete("/categories/{id}", deleteCategoryId))
                .andExpect(status().isNoContent());
    }

    private CreateCategoryRequestDto getCategoryRequestDto() {
        return new CreateCategoryRequestDto(
                "Sample Category Name",
                "Sample Category Description"
        );
    }

    private CreateCategoryRequestDto getUpdateCategoryRequestDto() {
        return new CreateCategoryRequestDto(
                "Sample Update Category Name",
                "Sample Update Category Description"
        );
    }

    private CategoryDto getCategoryDtoFromRequestDto(CreateCategoryRequestDto categoryRequestDto) {
        return new CategoryDto(
                null,
                categoryRequestDto.name(),
                categoryRequestDto.description());
    }

    private List<CategoryDto> getListCategoryDto() {
        CategoryDto firstCategoryDto =
                new CategoryDto(1L, "Science Fiction", "books about technology");

        CategoryDto secondCategoryDto =
                new CategoryDto(2L, "Detectives", "books about deduction");

        return List.of(firstCategoryDto, secondCategoryDto);
    }

    private List<BookDtoWithoutCategoryIds> getListBookDtoWithoutCategoryIds() {
        BookDtoWithoutCategoryIds firstBookDto = new BookDtoWithoutCategoryIds();
        firstBookDto.setTitle("Robots and Empire");
        firstBookDto.setAuthor("Isaac Asimov");
        firstBookDto.setIsbn("9780008277796");
        firstBookDto.setPrice(BigDecimal.valueOf(360.000)
                .setScale(3, RoundingMode.HALF_UP));
        firstBookDto.setDescription("This is a sample book description. 1");
        firstBookDto.setCoverImage("http://example.com/cover1.jpg");

        return List.of(firstBookDto);
    }
}
