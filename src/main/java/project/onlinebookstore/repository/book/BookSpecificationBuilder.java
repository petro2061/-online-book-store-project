package project.onlinebookstore.repository.book;

import java.math.BigDecimal;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import project.onlinebookstore.dto.BookSearchParameters;
import project.onlinebookstore.model.Book;
import project.onlinebookstore.repository.SpecificationBuilder;
import project.onlinebookstore.repository.SpecificationProviderManager;

@Component
@RequiredArgsConstructor
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {

    private final SpecificationProviderManager<Book> specificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParameters searchParameters) {
        Specification<Book> spec = Specification.where(null);

        if (searchParameters.titles() != null
                && searchParameters.titles().length > 0) {
            spec = spec.and(specificationProviderManager
                    .getSpecificationProvider("title")
                    .getSpecification(searchParameters.titles()));
        }

        if (searchParameters.prices() != null
                && searchParameters.prices().length > 0) {
            String[] bigDecimalPriceToString =
                    Arrays.stream(searchParameters.prices())
                            .map(BigDecimal::toString)
                            .toArray(String[]::new);
            spec = spec.and(specificationProviderManager
                    .getSpecificationProvider("price")
                    .getSpecification(bigDecimalPriceToString));
        }

        if (searchParameters.isbnes() != null
                && searchParameters.isbnes().length > 0) {
            spec = spec.and(specificationProviderManager
                    .getSpecificationProvider("isbn")
                    .getSpecification(searchParameters.isbnes()));
        }

        if (searchParameters.authors() != null
                && searchParameters.authors().length > 0) {
            spec = spec.and(specificationProviderManager
                    .getSpecificationProvider("author")
                    .getSpecification(searchParameters.authors()));
        }
        return spec;
    }
}
