package project.onlinebookstore.validation.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Objects;
import org.springframework.beans.BeanWrapperImpl;
import project.onlinebookstore.validation.FieldMatch;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {
    private String firstFieldName;
    private String secondFieldName;

    @Override
    public void initialize(FieldMatch constraintAnnotation) {
        this.firstFieldName = constraintAnnotation.first();
        this.secondFieldName = constraintAnnotation.second();
    }

    @Override
    public boolean isValid(Object value,
                           ConstraintValidatorContext constraintValidatorContext) {
        Object firstValue
                = new BeanWrapperImpl(value).getPropertyValue(this.firstFieldName);
        Object secondValue =
                new BeanWrapperImpl(value).getPropertyValue(this.secondFieldName);
        return Objects.equals(firstValue, secondValue);
    }
}
