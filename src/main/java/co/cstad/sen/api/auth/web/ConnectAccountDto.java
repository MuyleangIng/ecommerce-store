package co.cstad.sen.api.auth.web;

import co.cstad.sen.api.auth.validation.ConstrainUserUuid;
import lombok.Builder;

@Builder
public record ConnectAccountDto(
        @ConstrainUserUuid
        String userUuid,
        String email,
        Long providerId,
        String providerName,
        String accessToken
) {
}
