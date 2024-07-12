package project.onlinebookstore.validation.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
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
        ExpressionParser expressionParser = new SpelExpressionParser();
        Object firstValue
                = expressionParser.parseExpression(firstFieldName).getValue(value);
        Object secondValue =
                expressionParser.parseExpression(secondFieldName).getValue(value);
        return firstValue == null && secondValue == null
                || firstValue != null && firstValue.equals(secondValue);
    }
}
