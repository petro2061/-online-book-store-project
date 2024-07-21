package project.onlinebookstore.repository.book.spec;

import jakarta.persistence.criteria.JoinType;
import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import project.onlinebookstore.model.Book;
import project.onlinebookstore.repository.SpecificationProvider;

public class CategoriesSpecificationProvider implements SpecificationProvider<Book> {
    private static final String CATEGORIES_FIELD_PARAMETER = "categories";
    private static final String INNER_FIELD_PARAMETER = "id";

    @Override
    public Specification<Book> getSpecification(String[] params) {

        return (root, query, criteriaBuilder) -> {
            root.fetch(CATEGORIES_FIELD_PARAMETER, JoinType.LEFT);
            return root
                    .get(CATEGORIES_FIELD_PARAMETER)
                    .get(INNER_FIELD_PARAMETER)
                    .in(Arrays.stream(params).toList());
        };
    }

    @Override
    public String getKey() {
        return CATEGORIES_FIELD_PARAMETER;
    }
}
