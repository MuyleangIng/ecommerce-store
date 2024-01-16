package co.cstad.sen.security.currentuser;

import co.cstad.sen.api.users.User;
import org.springframework.security.core.Authentication;

public interface IAuthenticationFacade {
    Authentication getAuthentication();

    User getAuthMe();
}
