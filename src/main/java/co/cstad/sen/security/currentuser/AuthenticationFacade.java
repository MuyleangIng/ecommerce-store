package co.cstad.sen.security.currentuser;


import co.cstad.sen.api.users.User;
import co.cstad.sen.api.users.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
@AllArgsConstructor
@Slf4j
public class AuthenticationFacade implements IAuthenticationFacade {
    private final UserRepository userRepository;


    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    public User getAuthMe() {
        Authentication authentication = getAuthentication();
        return userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Auth me("+ authentication.getName() +") is not found."));
    }
}
