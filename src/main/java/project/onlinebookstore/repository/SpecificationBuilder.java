package project.onlinebookstore.repository;

import org.springframework.data.jpa.domain.Specification;
import project.onlinebookstore.dto.BookSearchParameters;

public interface SpecificationBuilder<T> {
    Specification<T> build(BookSearchParameters searchParameters);
}
