package project.onlinebookstore.repository.book.spec;

import jakarta.persistence.criteria.JoinType;
import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import project.onlinebookstore.model.Book;
import project.onlinebookstore.repository.SpecificationProvider;

@Component
public class AuthorSpecificationProvider implements SpecificationProvider<Book> {
    private static final String AUTHOR_FIELD_PARAMETER = "author";
    private static final String FETCH_FIELD_NAME = "categories";

    @Override
    public Specification<Book> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) -> {
            root.fetch(FETCH_FIELD_NAME, JoinType.LEFT);
            return root
                    .get(AUTHOR_FIELD_PARAMETER)
                    .in(Arrays.stream(params).toList());
        };
    }

    @Override
    public String getKey() {
        return AUTHOR_FIELD_PARAMETER;
    }
}
