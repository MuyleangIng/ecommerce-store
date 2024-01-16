package co.cstad.sen.api.auth.validation;

import co.cstad.sen.api.users.User;
import co.cstad.sen.api.users.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;


@AllArgsConstructor
@Slf4j
public class ConstrainIsVerifiedValidator implements ConstraintValidator<ConstrainIsVerified, String> {
    private final UserRepository userRepository;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        User user = userRepository.findIsVerifiedNotNullByEmail(value).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"The user is not fount"));
        return user.isVerified();
    }
}