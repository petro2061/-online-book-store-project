package project.onlinebookstore.dto;

import java.math.BigDecimal;

public record BookSearchParameters(
        String[] titles,
        String[] authors,
        String[] isbns,
        BigDecimal[] prices) {
}
