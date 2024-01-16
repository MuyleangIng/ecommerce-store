package co.cstad.sen.api.auth.validation;

import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,ElementType.METHOD})
public @interface UniqueEmail {
    String message() default "email is already exists";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
