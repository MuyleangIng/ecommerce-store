package co.cstad.sen.api.auth.validation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Constraint(validatedBy = UpdateEmailValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,ElementType.METHOD})
public @interface UpdateEmailConstraint {
    String message() default "Email is already exist";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
