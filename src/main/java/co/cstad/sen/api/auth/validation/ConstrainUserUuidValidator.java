package co.cstad.sen.api.auth.validation;

import co.cstad.sen.api.users.UserRepository;
import co.cstad.sen.utils.EncryptionUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@AllArgsConstructor
@Slf4j
public class ConstrainUserUuidValidator implements ConstraintValidator<ConstrainUserUuid, String> {
    private final UserRepository userRepository;

    @Override
    public boolean isValid(String uuid, ConstraintValidatorContext context) {
        String decryptedUuid = EncryptionUtil.decrypt(uuid, "user");
        log.info("decryptedUuid: {}", decryptedUuid);
        if (decryptedUuid!=null){
            Long id = Long.parseLong(decryptedUuid);
            return userRepository.findById(id).isPresent();
        }
        return false;
    }
}