package co.cstad.sen.api.auth;

import co.cstad.sen.api.auth.web.*;
import co.cstad.sen.api.cloud.CloudService;
import co.cstad.sen.api.role.Role;
import co.cstad.sen.api.role.RoleEnum;
import co.cstad.sen.api.role.RoleRepository;
import co.cstad.sen.api.users.User;
import co.cstad.sen.api.users.UserRepository;

import co.cstad.sen.utils.Notifications;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService{
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final DaoAuthenticationProvider daoAuthenticationProvider;
    private final JwtAuthenticationProvider jwtRefreshAuthenticationProvider;
    private final JwtEncoder accessTokenJwtEncoder;
    private JwtEncoder refreshTokenJwtEncoder;
    private final Notifications notifications;
    private final CloudService cloudService;
    private PasswordEncoder encoder;
    @Autowired
    public void setRefreshTokenJwtEncoder(@Qualifier("refreshTokenJwtEncoder") JwtEncoder refreshTokenJwtEncoder) {
        this.refreshTokenJwtEncoder = refreshTokenJwtEncoder;
    }

    @Autowired
    void setEncoder(PasswordEncoder encoder){
        this.encoder = encoder;
    }

    @Override
    public Boolean verify(VerifyDto verifyDto) {
        UUID uuid = UUID.fromString(verifyDto.verifyCode());
        if (!userRepository.existsByEmail(verifyDto.email()) || !userRepository.existsByVerifiedCode(uuid)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "You account not found.");
        }
        LocalDateTime now = LocalDateTime.now();
        userRepository.updateVerifiedByEmail(now, verifyDto.email());
        return true;
    }

    @Override
    public void checkVerify(String email, String code) {

    }

    @Override
    public LoginResponse refreshToken(RefreshToken rtoken) {
        Instant now = Instant.now();
        BearerTokenAuthenticationToken token = new BearerTokenAuthenticationToken(rtoken.refreshToken());
        Authentication auth = jwtRefreshAuthenticationProvider.authenticate(token);
        User user = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Email not found."));
        // Define scope
        String scope = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(a -> a.startsWith("ROLE_"))
                .collect(Collectors.joining(" "));

        JwtClaimsSet accessTokenJwtClaimsSet = JwtClaimsSet.builder()
                .issuedAt(now)
                .issuer("client")
                .expiresAt(now.plus(1, ChronoUnit.HOURS))
                .subject(auth.getName())
                .claim("scope", scope)
                .build();
        String t = accessTokenJwtEncoder.encode(
                JwtEncoderParameters.from(accessTokenJwtClaimsSet)
        ).getTokenValue();
        return LoginResponse.builder()
                .refreshToken(rtoken.refreshToken())
                .accessToken(t)
                .user(user)
                .build();
    }

    @Override
    public LoginResponse userLogin(LoginDto loginDto) {
        SocialEnum socialEnum = SocialEnum.valueOf(loginDto.loginKey());
        if (socialEnum != SocialEnum.CREDENTIALS){
            return this.loginWithSocial(loginDto.email(), socialEnum);
        }

        User user = userRepository.findByEmailOrUsername(loginDto.email(), loginDto.email())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Email not found.:findByEmailOrUsername"));

        if (user.getRoles().isEmpty()){
            Role role = roleRepository.findByName(RoleEnum.DEVELOPER.toString())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found."));
            user.setRoles(List.of(role));
            userRepository.save(user);
        }
        if(user.getProvider().compareTo(SocialEnum.CREDENTIALS) != 0){
            user.setProvider(socialEnum);
            user.setPassword(encoder.encode(loginDto.password()));
            userRepository.save(user);
        }
        return this.loginToken(user, loginDto,"credentials");
    }

    @Override
    public void setLastLogin(String email) {
        userRepository.updateLastLoginByEmail(email);
    }

    @Override
    public RegisterResponse userRegister(RegisterDto registerDto) {
        // Check if the username already exists in the database
        if (userRepository.existsByUsername(registerDto.username())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists.");
        }

        if (userRepository.existsByEmail(registerDto.email())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists.");
        }

        Role role = roleRepository.findByName(RoleEnum.DEVELOPER.toString())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found."));
        UUID verifiedCode = UUID.randomUUID();
        User user = new User(registerDto.username(), registerDto.email(), encoder.encode(registerDto.password()), verifiedCode);
        user.setRoles(List.of(role));
        user = userRepository.save(user);

        RegisterResponse registerResponse = RegisterResponse.builder()
                .user(user)
                .build();

        return registerResponse;
    }




    @Override
    public void forgotPassword(ForgotPasswordDto forgotPasswordDto) {
        User user = userRepository.findByEmail(forgotPasswordDto.email())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Email not found."));
        user.setVerifiedCode(UUID.randomUUID());
        user.setVerified(null);
        userRepository.save(user);
        Notifications.Meta<?> meta = Notifications.Meta.builder()
                .to(user.getEmail())
                .from(user.getEmail())
                .data(user)
                .subject("Forgot Password")
                .templateUrl("mail/reset-password")
                .build();
        notifications.sendEmail(meta, user);
        notifications.sendTelegramBot(
                user,
                "User Forgot Password",
                """
                        ID: <code>%s</code>
                        UUID: <code>%s</code>
                        Username: <code>%s</code>
                        Email: <code>%s</code>
                        lastLogin: <code>%s</code>
                        verifiedCode: <code>%s</code>
                        """.formatted(user.getId(), user.getUuid(), user.getUsername(), user.getEmail(), user.getLastLogin(), user.getVerifiedCode())
        );
    }

    @Override
    public void resetPassword(ResetPasswordDto resetPassword) {
        UUID uuid = UUID.fromString(resetPassword.verificationCode());
        User user = userRepository.findByEmailAndVerifiedCode(resetPassword.email(), uuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with verify code."));
        user.setPassword(encoder.encode(resetPassword.password()));
        user.setVerifiedCode(null);
        user.setVerified(LocalDateTime.now());
        userRepository.save(user);

    }

    private LoginResponse loginWithSocial(String email, SocialEnum socialEnum) {
        User user = userRepository.findByEmailUsernameProvider(email,"",socialEnum)
                .orElseGet(() -> userRepository.findByEmailOrUsername(email,"")
                        .orElseGet(() -> {
                            User newUser = new User();
                            Role role = roleRepository.findByName(RoleEnum.DEVELOPER.toString()).orElseThrow(() -> new RuntimeException("Role not found."));
                            newUser.setEmail(email);
                            newUser.setUsername(email);
                            newUser.setProvider(socialEnum);
                            newUser.setPassword(encoder.encode(email));
                            newUser.setIsDisabled(false);
                            newUser.setRoles(List.of(role));
                            return userRepository.save(newUser);
                        }));
        if (user.getRoles().isEmpty()){
            Role role = roleRepository.findByName(RoleEnum.DEVELOPER.toString())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found."));
            user.setRoles(List.of(role));
            userRepository.save(user);
        }

        return this.loginToken(user, LoginDto.builder().email(email).password(user.getUsername()).build(),"social");
    }

    private LoginResponse loginToken(User user, LoginDto loginDto,String method){
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(),loginDto.password());
        authentication = daoAuthenticationProvider.authenticate(authentication);
        Instant now = Instant.now();
        Instant refreshTokenExpiresAt = now.plus(30, ChronoUnit.DAYS);
        // Define scope
        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(a -> a.startsWith("ROLE_"))
                .collect(Collectors.joining(" "));
        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .subject(authentication.getName())
                .expiresAt(now.plus(10, ChronoUnit.HOURS))
                .claim("scope", scope)
                .build();

        JwtClaimsSet refreshTokenClaimsSet = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .subject(authentication.getName())
                .expiresAt(refreshTokenExpiresAt)
                .claim("scope", scope)
                .build();
        String refreshToken=null, accessToken = null;
        if (!method.equals("social")){
            refreshToken = refreshTokenJwtEncoder.encode(JwtEncoderParameters.from(refreshTokenClaimsSet)).getTokenValue();
            accessToken = accessTokenJwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();
        }
        log.info("accessToken: {}",accessToken);
        this.setLastLogin(loginDto.email());
        return LoginResponse.builder()
                .refreshToken(refreshToken)
                .accessToken(accessToken)
                .user(user)
                .build();
    }

    private void sendNotifications(User user, String subject, String message) {
        Notifications.Meta<?> meta = Notifications.Meta.builder()
                .to(user.getEmail())
                .from(user.getEmail())
                .data(user)
                .subject(subject)
                .templateUrl("mail/verify")
                .build();
        notifications.sendEmail(meta, user);

        notifications.sendTelegramBot(user, subject, message);
    }


}
