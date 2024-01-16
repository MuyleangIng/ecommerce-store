package co.cstad.sen.api.auth.web;

import co.cstad.sen.api.users.User;
import lombok.Builder;

@Builder
public record RegisterResponse(
        User user
) {
}
