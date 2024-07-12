package project.onlinebookstore.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import project.onlinebookstore.validation.impl.PasswordValidation;

@Constraint(validatedBy = PasswordValidation.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Password {
    String message() default "The password must include letters, "
            + "numbers and special symbols. "
            + "Please check the correctness of the entered data!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
