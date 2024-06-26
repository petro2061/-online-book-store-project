package project.onlinebookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.onlinebookstore.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

}
