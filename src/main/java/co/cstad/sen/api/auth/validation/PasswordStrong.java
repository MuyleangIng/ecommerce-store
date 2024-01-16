package co.cstad.sen.api.auth.validation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = PasswordStrongConstrainValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface PasswordStrong {

    String message() default "The password field is so weak.";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}