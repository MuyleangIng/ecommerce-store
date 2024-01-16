package co.cstad.sen.api.auth.validation;

import co.cstad.sen.api.users.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ConstrainEmailOrUsernameValidator implements ConstraintValidator<ConstrainEmailOrUsername, String> {
    private final UserRepository userRepository;
    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return userRepository.findByEmailOrUsername(email, email).isPresent();
    }
}