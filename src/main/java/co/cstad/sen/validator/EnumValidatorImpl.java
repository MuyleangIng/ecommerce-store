package co.cstad.sen.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.ArrayList;
import java.util.List;

public class EnumValidatorImpl implements ConstraintValidator<EnumValidator, CharSequence> {
    private List<String> acceptedValues;
    @Override
    public void initialize(EnumValidator annotation) {
        acceptedValues = new ArrayList<>();
        Class<? extends Enum<?>> enumClass = annotation.enumClass();
        Enum<?>[] enumValArr = enumClass.getEnumConstants();

        for (Enum<?> enumVal : enumValArr) {
            acceptedValues.add(enumVal.toString().toUpperCase());
        }
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        return acceptedValues.contains(value.toString().toUpperCase());
    }
}