package co.cstad.sen.api.auth.validation;

import co.cstad.sen.api.users.UserRepository;
import co.cstad.sen.security.currentuser.IAuthenticationFacade;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UpdateEmailValidator implements ConstraintValidator<UpdateEmailConstraint, String> {
    private final UserRepository userRepository;
    private IAuthenticationFacade iAuthenticationFacade;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return userRepository.findByEmail(value).isEmpty() || iAuthenticationFacade.getAuthMe().getEmail().equals(value);
    }
}