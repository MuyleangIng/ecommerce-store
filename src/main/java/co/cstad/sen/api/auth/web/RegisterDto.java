package co.cstad.sen.api.auth.web;

import co.cstad.sen.api.auth.validation.PasswordMatch;
import co.cstad.sen.api.auth.validation.PasswordStrong;
import co.cstad.sen.api.auth.validation.UniqueEmail;
import co.cstad.sen.api.auth.validation.UniqueUsername;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
@PasswordMatch
@Schema(name = "Register", description = "Register")
public record RegisterDto(
        @NotBlank(message = "The field username is required.")
        @Size(min = 6, max = 50, message = "Username must be between 6 and 50 characters")
        @Schema(name = "username", description = "username", defaultValue = "cstad1234")
        @UniqueUsername
        @Pattern(regexp = "^[a-z-0-9]*$", message = "Username must be alphanumeric with no spaces")
        String username,
        @NotBlank(message = "The field email is required.")
        @Schema(name = "email", description = "email", defaultValue = "cstad1234@gmail.com")
        @Email
        @UniqueEmail
        String email,
        @NotBlank(message = "The field password is required.")
        @Schema(name = "password", description = "password", defaultValue = "Admin@123")
        @PasswordStrong
        String password,
        @NotBlank(message = "The field confirm_password is required.")
        @Schema(name = "confirmPassword", description = "confirm Password", defaultValue = "Admin@123")
        @PasswordStrong
        String confirmPassword
) {
}