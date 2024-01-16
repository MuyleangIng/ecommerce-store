package co.cstad.sen.api.auth.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = LoginKeyConstraintValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,ElementType.METHOD})
public @interface LoginKeyConstraint {
    String message() default "loginKey is not valid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
