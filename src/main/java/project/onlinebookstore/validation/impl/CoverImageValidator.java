package project.onlinebookstore.validation.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;
import project.onlinebookstore.validation.CoverImage;

public class CoverImageValidator implements ConstraintValidator<CoverImage, String> {
    private static final String IMAGE_URL_PATTERN =
            "^(https?://.*\\.(?:png|jpg|jpeg|gif|bmp))$";

    @Override
    public boolean isValid(String coverImage,
                           ConstraintValidatorContext constraintValidatorContext) {
        return coverImage != null
                && Pattern.compile(IMAGE_URL_PATTERN).matcher(coverImage).matches();
    }
}
