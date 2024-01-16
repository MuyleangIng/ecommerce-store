package co.cstad.sen.api.auth.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = PasswdMatchValidator.class)
@Retention(RUNTIME)
@Target(ElementType.TYPE)
public @interface PasswordMatch {
    String message() default "The password field is not match confirm password.";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    String password() default "password";

    String confirmPassword() default "confirmPassword";

    @Target({ ElementType.TYPE })
    @Retention(RUNTIME)
    @Documented
    @interface List {
        PasswordMatch[] value();
    }
}
