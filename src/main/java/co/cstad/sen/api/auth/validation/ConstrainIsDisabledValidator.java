package co.cstad.sen.api.auth.validation;

import co.cstad.sen.api.auth.web.SocialEnum;
import co.cstad.sen.api.users.UserRepository;
import co.cstad.sen.utils.EncryptionUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanWrapperImpl;


@Slf4j
public class ConstrainIsDisabledValidator implements ConstraintValidator<ConstrainIsDisabled, Object> {
    private final UserRepository userRepository;

    private String message;
    private String email;
    private String loginKey;

    public ConstrainIsDisabledValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public void initialize(ConstrainIsDisabled constraintAnnotation) {
        this.email = constraintAnnotation.email();
        this.loginKey = constraintAnnotation.loginKey();
        this.message = constraintAnnotation.message();
    }


    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Object email = new BeanWrapperImpl(value).getPropertyValue(this.email);
        Object loginKey = new BeanWrapperImpl(value).getPropertyValue(this.loginKey);
        boolean isValid=false;
        if (loginKey == null || loginKey.toString().isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("The loginKey field is required.")
                    .addPropertyNode(this.loginKey)
                    .addConstraintViolation();
            return false;
        }
        String key = EncryptionUtil.decrypt(loginKey.toString(),LoginKeyConstraintValidator.LOGIN_KEY);
        boolean isEmpty = userRepository.findByEmailOrUsername(email.toString(), email.toString()).isEmpty();
        if (isEmpty && key.equals(SocialEnum.CREDENTIALS.toString())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Your account does not exist.")
                    .addPropertyNode(this.email)
                    .addConstraintViolation();
            return false;
        }
        boolean active = userRepository.findVerifiedNullOrIsDisabledTrueByEmail(email.toString()).isPresent();
        if (active && key.equals(SocialEnum.CREDENTIALS.toString())){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Your account is not active.")
                    .addPropertyNode(this.email)
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}