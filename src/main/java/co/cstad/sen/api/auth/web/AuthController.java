package co.cstad.sen.api.auth.web;

import co.cstad.sen.api.auth.AuthService;
import co.cstad.sen.api.auth.validation.LoginKeyConstraintValidator;
import co.cstad.sen.api.users.User;
import co.cstad.sen.security.currentuser.IAuthenticationFacade;
import co.cstad.sen.utils.EncryptionUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/auth")
@ApiResponses(value = {
        @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "405", content = @Content(schema = @Schema(hidden = true))),
})
@Tag(name = "Auth")
@Slf4j
public class AuthController {
    private final AuthService authService;
    private final IAuthenticationFacade iAuthenticationFacade;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Parameter(name = "json", required = true,schema = @Schema(implementation = LoginDto.class,allowableValues = {"email", "password"}))
            @Valid @RequestBody LoginDto loginDto
    ) {
        LoginDto l = LoginDto.builder()
                .email(loginDto.email())
                .password(loginDto.password())
                .loginKey(EncryptionUtil.decrypt(loginDto.loginKey(), LoginKeyConstraintValidator.LOGIN_KEY))
                .build();
        LoginResponse loginResponse = authService.userLogin(l);
        return new ResponseEntity<>(loginResponse,HttpStatus.OK);
    }


    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
            @RequestBody @Valid RegisterDto registerDto
    ){
        RegisterResponse registerResponse = authService.userRegister(registerDto);
        return new ResponseEntity<>(registerResponse,HttpStatus.OK);
    }

    @GetMapping("/me")
    @Operation(security = { @SecurityRequirement(name = "accessToken") })
    public ResponseEntity<User> me(){
        return new ResponseEntity<>(iAuthenticationFacade.getAuthMe(),HttpStatus.OK);
    }


    @PostMapping("/refresh-token")
    public ResponseEntity<LoginResponse> refreshToken(@RequestBody RefreshToken token){
        return new ResponseEntity<>(authService.refreshToken(token),HttpStatus.OK);
    }


    @GetMapping("/verify")
    public ResponseEntity<Boolean> verifyAccount(
            @RequestParam String email,
            @RequestParam String verifyCode
    ) {
        VerifyDto verifyDto = VerifyDto.builder()
                .email(email)
                .verifyCode(verifyCode)
                .build();
        return new ResponseEntity<>(authService.verify(verifyDto),HttpStatus.OK);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String,Object>> forgotPassword(
            @RequestBody @Valid ForgotPasswordDto forgotPasswordDto
    ){
        authService.forgotPassword(forgotPasswordDto);
        return ResponseEntity.ok(new HashMap<>(){{
            put("status",true);
            put("message", "Please check your email.");
        }});
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Map<String,Object>> resetPassword(
            @RequestBody @Valid ResetPasswordDto resetPasswordDto
    ){
        authService.resetPassword(resetPasswordDto);
        return ResponseEntity.ok(new HashMap<>(){{
            put("status",true);
            put("message", "Password reset successfully.");
        }});
    }
}
