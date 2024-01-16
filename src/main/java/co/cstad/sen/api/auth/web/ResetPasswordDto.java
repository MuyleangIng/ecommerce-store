package co.cstad.sen.api.auth.web;

import co.cstad.sen.api.auth.validation.EmailExists;
import co.cstad.sen.api.auth.validation.PasswordMatch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
@PasswordMatch
public record ResetPasswordDto(
        @NotBlank(message = "The field verificationCode is required.")
        String verificationCode,
        @EmailExists
        @Email
        String email,
        @NotBlank(message = "The field password is required.")
        @Size(min = 6, max = 15, message = "Password must be between 6 and 15 characters")
        String password,
        @Size(min = 6, max = 15, message = "Password must be between 6 and 15 characters")
        String confirmPassword
) {
}
