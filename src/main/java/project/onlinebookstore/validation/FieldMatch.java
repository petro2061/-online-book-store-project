package project.onlinebookstore.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import project.onlinebookstore.validation.impl.FieldMatchValidator;

@Constraint(validatedBy = FieldMatchValidator.class)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldMatch {
    String message() default "Repeated password does not match the entered one. "
            + "Please check the correctness of the entered data!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String first();
    String second();
}
