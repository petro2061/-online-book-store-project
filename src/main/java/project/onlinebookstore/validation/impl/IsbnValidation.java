package project.onlinebookstore.validation.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;
import project.onlinebookstore.validation.Isbn;

public class IsbnValidation implements ConstraintValidator<Isbn, String> {
    private static final String ISBN_VALID_PATTERN = "[0-9\\-]+";

    @Override
    public boolean isValid(String isbn,
                           ConstraintValidatorContext constraintValidatorContext) {
        return isbn != null
                && Pattern.compile(ISBN_VALID_PATTERN).matcher(isbn).matches();
    }
}
