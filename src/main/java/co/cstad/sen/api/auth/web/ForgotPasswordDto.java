package co.cstad.sen.api.auth.web;

import co.cstad.sen.api.auth.validation.EmailExists;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record ForgotPasswordDto(
        @NotBlank(message = "Email is required")
        @EmailExists
        @Email(message = "Email should be valid")
        String email
) {
}
