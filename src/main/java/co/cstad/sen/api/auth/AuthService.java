package co.cstad.sen.api.auth;

import co.cstad.sen.api.auth.web.*;

public interface AuthService {
    Boolean verify(VerifyDto verifyDto);

    void checkVerify(String email, String code);

    LoginResponse refreshToken(RefreshToken refreshToken);

    LoginResponse userLogin(LoginDto loginDto);

    void setLastLogin(String email);

    RegisterResponse userRegister(RegisterDto registerDto);



    void forgotPassword(ForgotPasswordDto forgotPasswordDto);

    void resetPassword(ResetPasswordDto resetPassword);
}
