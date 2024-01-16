

package co.cstad.sen.api.auth.validation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Constraint(validatedBy = UpdateUsernameValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,ElementType.METHOD})
public @interface UpdateUsernameConstraint {
    String message() default "Username is already exist";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
