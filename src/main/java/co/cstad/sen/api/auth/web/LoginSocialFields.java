package co.cstad.sen.api.auth.web;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
@Schema(name = "Login Social Fields", description = "LoginSocialFields")
public record LoginSocialFields(
        @NotBlank(message = "The field firstName is required.")
        @Schema(name = "firstName", description = "firstName", defaultValue = "Begoingto")
        String firstName,
        @NotBlank(message = "The field lastName is required.")
        @Schema(name = "lastName", description = "lastName", defaultValue = "Me")
        String lastName,
        @NotBlank(message = "The field email is required.")
        @Schema(name = "email", description = "email", defaultValue = " ")
        String email,
        @NotBlank(message = "The field username is required.")
        @Schema(name = "username", description = "username", defaultValue = " ")
        String username,
        @NotBlank(message = "The field avatar is required.")
        @Schema(name = "avatar", description = "avatar", defaultValue = " ")
        String avatar,
        @NotBlank(message = "The field provider is required.")
        @Schema(name= "provider", description = "provider", defaultValue ="GITHUB")
        String provider,
        @NotNull(message = "The field providerId is required.")
        @Schema(name = "providerId", description = "providerId", defaultValue = " ")
        Long providerId,
        @NotBlank(message = "The field providerToken is required.")
        @Schema(name = "providerToken", defaultValue = " ", description = "providerToken")
        String providerToken
) {
}
