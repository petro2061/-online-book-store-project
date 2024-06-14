package project.onlinebookstore;

import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import project.onlinebookstore.model.Book;
import project.onlinebookstore.service.BookService;

@SpringBootApplication
public class OnlineBookStoreApplication {
    @Autowired
    private BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(OnlineBookStoreApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            Book testBook = new Book();
            testBook.setTitle("Harry Potter and the Deathly Hallows");
            testBook.setAuthor("J. K. Rowling");
            testBook.setIsbn("1");
            testBook.setPrice(BigDecimal.valueOf(120));

            bookService.save(testBook);
            System.out.println(bookService.findAll());
        };
    }
}
