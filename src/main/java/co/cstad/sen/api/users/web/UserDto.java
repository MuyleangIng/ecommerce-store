package co.cstad.sen.api.users.web;

import co.cstad.sen.api.role.Role;

import java.sql.Timestamp;

public record UserDto(
        Long id,
        Role role_id,
        String uuid,
        String avatar,
        String first_name,
        String last_name,
        String username,
        String email,
        String password,
        Boolean isVerified,
        Boolean isDisabled,
        Timestamp deleted_at,
        Timestamp created_at,
        Timestamp updated_at,
        Timestamp last_login,
        String verified_code_expiry
) {


}
