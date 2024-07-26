package project.onlinebookstore.repository.book.spec;

import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import project.onlinebookstore.model.Book;
import project.onlinebookstore.repository.SpecificationProvider;

@Component
public class PriceSpecificationProvider implements SpecificationProvider<Book> {
    private static final String PRICE_FIELD_PARAMETER = "price";
    private static final String FETCH_FIELD_NAME = "categories";
    private static final int PRICE_INDEX_MIN = 0;
    private static final int PRICE_INDEX_MAX = 1;

    @Override
    public Specification<Book> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) -> {
            root.fetch(FETCH_FIELD_NAME, JoinType.LEFT);
            return criteriaBuilder
                    .between(root.get(PRICE_FIELD_PARAMETER),
                            params[PRICE_INDEX_MIN],
                            params[PRICE_INDEX_MAX]);
        };
    }

    @Override
    public String getKey() {
        return PRICE_FIELD_PARAMETER;
    }
}
