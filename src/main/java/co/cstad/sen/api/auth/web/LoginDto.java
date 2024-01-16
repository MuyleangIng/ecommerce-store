package co.cstad.sen.api.auth.web;

import co.cstad.sen.api.auth.validation.ConstrainIsDisabled;
import co.cstad.sen.api.auth.validation.LoginKeyConstraint;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
@Schema(name = "LoginDto", description = "LoginDto")
@ConstrainIsDisabled
public record LoginDto(
        @Schema(name = "email", description = "email", defaultValue = "developer@gmail.com")
        @NotBlank(message = "The field email is required.")
        String email,
        @NotBlank(message = "The field password is required.")
        @Schema(name = "password", description = "password", defaultValue = "password")
        String password,

        @Schema(name = "loginKey", description = "loginKey:Y71o29qo8RIwIBMRClJWfg==, NDNunycQML0XbpFnJag2ig==, K9E2EMI_0b2EZn14n1UoEw==, J7EW7s7mExRFa1sSzoC93w==", defaultValue = "Y71o29qo8RIwIBMRClJWfg==")
        @LoginKeyConstraint
        String loginKey
) {
}
