package io.incondensable.application.business;

import io.incondensable.application.business.domain.entity.User;
import io.incondensable.application.business.exceptions.auth.ForgotPasswordsMismatchException;
import io.incondensable.application.business.service.login.LoginService;
import io.incondensable.application.business.service.logout.LogoutService;
import io.incondensable.application.business.service.otp.OtpService;
import io.incondensable.application.business.service.user.UserService;
import io.incondensable.application.mapper.UserMapper;
import io.incondensable.application.web.dto.req.auth.CompleteUserSignupRequestDTO;
import io.incondensable.application.web.dto.req.credentials.ForgotPasswordRequestDTO;
import io.incondensable.application.web.dto.req.auth.UserLoginRequestDTO;
import io.incondensable.application.web.dto.res.UserLoginResponseDTO;
import io.incondensable.application.web.dto.res.UserResponseDTO;
import org.springframework.stereotype.Service;

/**
 * @author abbas
 */
@Service
public class AuthenticationFacade {

    private final OtpService otpService;
    private final UserMapper userMapper;
    private final UserService userService;
    private final LoginService loginService;
    private final LogoutService logoutService;

    public AuthenticationFacade(UserService userService, LoginService loginService, LogoutService logoutService, OtpService otpService, UserMapper userMapper) {
        this.userService = userService;
        this.loginService = loginService;
        this.logoutService = logoutService;
        this.otpService = otpService;
        this.userMapper = userMapper;
    }

    public UserLoginResponseDTO loginUser(UserLoginRequestDTO request) {
        return loginService.validateUser(request)
                .map(
                        loginService::login
                ).map(u -> new UserLoginResponseDTO(
                        u.getId(),
                        u.getToken().getJwtString(),
                        u.getUsername(),
                        u.getRole().getRoleStr()
                )).get();
    }

    /**
     * in fact, in the first step, we will use create-user API, but the User is NOT Enabled,
     * then the Client, by receiving the OTP Code, and entering it in this service, its
     * SignUp flow gets completed.
     *
     * @return the DTO of the recently-Enabled-User.
     */
    public UserResponseDTO completeSignUp(CompleteUserSignupRequestDTO request) {
        User user = userService.getByEmail(request.getEmail());
        otpService.validateOtpCode(user, request.getOptCode());
        user = userService.activateUser(user);
        return userMapper.mapEntityToDto(user);
    }

    public void initializeForgotPassword(String email) {
        User user = userService.getByEmail(email);
        otpService.sendOtpCode(user, "Forgot Password");
    }

    public UserResponseDTO forgotPassword(ForgotPasswordRequestDTO req) {
        if (!req.getNewPassword().equals(req.getNewPasswordConfirm()))
            throw new ForgotPasswordsMismatchException();

        User user = userService.getByEmail(req.getEmail());
        otpService.validateOtpCode(user, req.getOtpCode());
        return userMapper.mapEntityToDto(
                userService.changeUserPassword(user, req.getNewPassword())
        );
    }

    public void changePassword() {

    }

    public void logout(String jwtToken) {
        logoutService.logout(jwtToken);
    }
}
