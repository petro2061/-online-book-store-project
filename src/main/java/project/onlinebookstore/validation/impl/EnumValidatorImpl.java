package project.onlinebookstore.validation.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import project.onlinebookstore.validation.ValidEnum;

public class EnumValidatorImpl implements ConstraintValidator<ValidEnum, String> {
    private Enum<?>[] enumValues;

    @Override
    public void initialize(ValidEnum constraintAnnotation) {
        this.enumValues = constraintAnnotation.enumClass().getEnumConstants();
    }

    @Override
    public boolean isValid(String enumName, ConstraintValidatorContext constraintValidatorContext) {
        for (Enum<?> value : enumValues) {
            if (value.name().equals(enumName)) {
                return true;
            }
        }
        return false;
    }
}
