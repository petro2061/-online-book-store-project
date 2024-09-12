package project.onlinebookstore.repository.book;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;
import project.onlinebookstore.model.Book;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("Get all books by valid category id")
    @Sql(scripts = {
            "classpath:database/insert-into-categories.sql",
            "classpath:database/insert-into-books.sql",
            "classpath:database/insert-into-books_categories.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/delete-all-from-categories.sql",
            "classpath:database/delete-all-from-books.sql",
            "classpath:database/delete-all-from-books_categories.sql",
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void testFindAllByCategoryId_withValidCategoryId_shouldReturnAllBookById() {
        //Given
        Pageable pageable = PageRequest.of(0, 10);
        Long categoryId = 2L;

        //When
        List<Book> allByCategoryId = bookRepository
                .findAllByCategoryId(categoryId, pageable);

        //Then
        assertEquals(1, allByCategoryId.size());
        assertEquals("Murder on the Orient Express",
                allByCategoryId.get(0).getTitle());
    }
}
