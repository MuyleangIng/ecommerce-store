package co.cstad.sen.api.auth.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EmailExistsValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface EmailExists {
    String message() default "Email does not exist";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

