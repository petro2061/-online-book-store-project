package project.onlinebookstore.repository.book;

import java.math.BigDecimal;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import project.onlinebookstore.dto.book.BookSearchParameters;
import project.onlinebookstore.model.Book;
import project.onlinebookstore.repository.SpecificationBuilder;
import project.onlinebookstore.repository.SpecificationProviderManager;

@Component
@RequiredArgsConstructor
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
    private static final String TITLE_KEY = "title";
    private static final String PRICE_KEY = "price";
    private static final String ISBN_KEY = "isbn";
    private static final String AUTHOR_KEY = "author";
    private static final String CATEGORIES_KEY = "categories";

    private final SpecificationProviderManager<Book> specificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParameters searchParameters) {
        Specification<Book> spec = Specification.where(null);

        if (searchParameters.titles() != null
                && searchParameters.titles().length > 0) {
            spec = spec.and(specificationProviderManager
                    .getSpecificationProvider(TITLE_KEY)
                    .getSpecification(searchParameters.titles()));
        }

        if (searchParameters.prices() != null
                && searchParameters.prices().length > 0) {
            String[] bigDecimalPriceToString =
                    Arrays.stream(searchParameters.prices())
                            .map(BigDecimal::toString)
                            .toArray(String[]::new);
            spec = spec.and(specificationProviderManager
                    .getSpecificationProvider(PRICE_KEY)
                    .getSpecification(bigDecimalPriceToString));
        }

        if (searchParameters.isbns() != null
                && searchParameters.isbns().length > 0) {
            spec = spec.and(specificationProviderManager
                    .getSpecificationProvider(ISBN_KEY)
                    .getSpecification(searchParameters.isbns()));
        }

        if (searchParameters.authors() != null
                && searchParameters.authors().length > 0) {
            spec = spec.and(specificationProviderManager
                    .getSpecificationProvider(AUTHOR_KEY)
                    .getSpecification(searchParameters.authors()));
        }

        if (searchParameters.categories() != null
                && searchParameters.categories().length > 0) {
            String[] categoriesIdLongToString =
                    Arrays.stream(searchParameters.categories())
                            .map(String::valueOf)
                            .toArray(String[]::new);

            spec = spec.and(specificationProviderManager
                    .getSpecificationProvider(CATEGORIES_KEY)
                    .getSpecification(categoriesIdLongToString));
        }
        return spec;
    }
}
