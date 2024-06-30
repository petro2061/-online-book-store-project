package project.onlinebookstore.validation.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;
import project.onlinebookstore.validation.Author;

public class AuthorValidator implements ConstraintValidator<Author, String> {
    private static final String AUTHOR_VALID_PATTERN = "[a-zA-Z0-9., ]+";

    @Override
    public boolean isValid(String author,
                           ConstraintValidatorContext constraintValidatorContext) {
        return author != null
                && Pattern.compile(AUTHOR_VALID_PATTERN).matcher(author).matches();
    }
}
