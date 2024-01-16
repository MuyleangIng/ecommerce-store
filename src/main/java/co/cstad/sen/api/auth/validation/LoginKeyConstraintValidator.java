package co.cstad.sen.api.auth.validation;

import co.cstad.sen.api.auth.web.SocialEnum;
import co.cstad.sen.utils.EncryptionUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class LoginKeyConstraintValidator implements ConstraintValidator<LoginKeyConstraint, String> {
    public static final String LOGIN_KEY = "LogOn";
    @Override
    public boolean isValid(String loginKey, ConstraintValidatorContext context) {
        loginKey = EncryptionUtil.decrypt(loginKey, LOGIN_KEY);
        try {
            SocialEnum.valueOf(loginKey);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}