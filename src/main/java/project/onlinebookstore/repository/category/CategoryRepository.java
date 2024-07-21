package project.onlinebookstore.repository.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.onlinebookstore.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
