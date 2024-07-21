package project.onlinebookstore.dto.book;

import java.math.BigDecimal;

public record BookSearchParameters(
        String[] titles,
        String[] authors,
        String[] isbns,
        BigDecimal[] prices,
        Long[] categories) {
}
