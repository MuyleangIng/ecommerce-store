package co.cstad.sen.api.auth.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UpdatePasswordValidator implements ConstraintValidator<UpdatePasswordConstraint, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return !((value.length() > 1) && (value.length() < 8));
    }
}