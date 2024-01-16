package co.cstad.sen;

import co.cstad.sen.api.auth.web.SocialEnum;
import co.cstad.sen.api.role.RoleEnum;
import co.cstad.sen.api.role.RoleRepository;
import co.cstad.sen.api.role.Role;
import co.cstad.sen.api.users.User;
import co.cstad.sen.api.users.UserRepository;
import co.cstad.sen.base.GenderEnum;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;

@Component
@Profile("dev")
public class DataInitializer implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private PasswordEncoder encoder;
    @Autowired
    void setEncoder(PasswordEncoder encoder){
        this.encoder = encoder;
    }
    public DataInitializer(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }
    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Create roles
        initializeRole(RoleEnum.ADMIN.name());
        initializeRole(RoleEnum.DEVELOPER.name());

        // Create users
        initializeUser(1,"admin","begoingto","admin","admin@gmail.com", "Admin@123", "ADMIN");
        initializeUser(2,"user", "developer","developer","developer@gmail.com", "password", "DEVELOPER");
    }

    private void initializeRole(String roleName) {
       roleRepository.findByName(roleName)
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName(roleName);
                    return roleRepository.save(role);
                });
    }

    private void initializeUser(Integer gitId,String firstName, String lastName,String username, String email, String password, String roleName) {
        LocalDateTime now = LocalDateTime.now();
        userRepository.findByEmailUsernameProvider(email,username, SocialEnum.CREDENTIALS)
                .orElseGet(() -> {
                    Role role = roleRepository.findByName(roleName).orElseThrow(
                            () -> new IllegalStateException("Role not found: " + roleName)
                    );
                    User user = new User(gitId,firstName,lastName,username, email, encoder.encode(password), now);
                    user.setRoles(List.of(role));
                    user.setGender(GenderEnum.MALE);
                    return userRepository.save(user);
                });
    }
}
