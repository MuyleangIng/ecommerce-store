package co.cstad.sen.api.auth.web;

import co.cstad.sen.api.users.User;
import lombok.Builder;


@Builder
public record LoginResponse(
        String refreshToken,
        String accessToken,
        Integer gitId,
        String gitAccessToken,
        User user
) {
}