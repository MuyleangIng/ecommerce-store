package co.cstad.sen.api.auth.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = ConstrainIsDisabledValidator.class)
@Retention(RUNTIME)
@Target(ElementType.TYPE)
public @interface ConstrainIsDisabled {
    String message() default "Your account is not active.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String email() default "email";

    String loginKey() default "loginKey";

    @Target({ ElementType.TYPE })
    @Retention(RUNTIME)
    @Documented
    @interface List {
        ConstrainIsDisabled[] value();
    }
}