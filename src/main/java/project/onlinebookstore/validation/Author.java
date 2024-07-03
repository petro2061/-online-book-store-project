package project.onlinebookstore.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import project.onlinebookstore.validation.impl.AuthorValidator;

@Constraint(validatedBy = AuthorValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Author {
    String message() default "The author's name is incorrect. "
            + "Please check the correctness of the entered data!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
