package co.cstad.sen.api.auth.validation;

import co.cstad.sen.api.users.UserRepository;
import co.cstad.sen.security.currentuser.IAuthenticationFacade;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UpdateUsernameValidator implements ConstraintValidator<UpdateUsernameConstraint, String> {
    private final UserRepository userRepository;
    private final IAuthenticationFacade iAuthenticationFacade;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return userRepository.findByUsername(value).isEmpty() || iAuthenticationFacade.getAuthMe().getUsername().equals(value);
    }
}