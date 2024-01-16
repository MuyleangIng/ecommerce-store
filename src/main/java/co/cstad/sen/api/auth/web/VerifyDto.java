package co.cstad.sen.api.auth.web;

import lombok.Builder;

@Builder
public record VerifyDto(
        String email,
        String verifyCode
) {
}
