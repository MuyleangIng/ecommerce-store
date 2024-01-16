package co.cstad.sen.api.users.web;

import co.cstad.sen.api.auth.validation.UniqueEmail;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record ModifyUserDto(
        String avatar,
        @NotBlank(message = "Firstname is required!")
        String firstName,
        @NotBlank(message = "Lastname is required!")
        String lastName,
        @NotBlank(message = "Username is required!")
        String username,
        @NotBlank(message = "The field email is required.")
        @Email
        @UniqueEmail
        @Size(max = 254)
        String email,
        @NotBlank(message = "Password is required!")
        String password,
        @NotNull(message = "Role is required!")
        Integer roleId
) {
}
