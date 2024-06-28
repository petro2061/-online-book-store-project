package project.onlinebookstore.repository.book.spec;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import project.onlinebookstore.model.Book;
import project.onlinebookstore.repository.SpecificationProvider;

@Component
public class PriceSpecificationProvider implements SpecificationProvider<Book> {
    private static final int PRICE_INDEX_MIN = 0;
    private static final int PRICE_INDEX_MAX = 1;

    @Override
    public Specification<Book> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get("price"),
                params[PRICE_INDEX_MIN],
                params[PRICE_INDEX_MAX]);
    }

    @Override
    public String getKey() {
        return "price";
    }
}
