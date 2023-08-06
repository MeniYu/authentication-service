package io.incondensable.application.web.controllers;

import io.incondensable.application.business.AuthenticationFacade;
import io.incondensable.application.business.domain.vo.auth.UserLoginResponseDTO;
import io.incondensable.application.web.dto.req.credentials.ForgotPasswordRequestDTO;
import io.incondensable.application.web.dto.req.login.UserLoginRequestDTO;
import io.incondensable.application.web.dto.res.UserResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author abbas
 */
@RestController
public class AuthController {

    private final AuthenticationFacade authFacade;

    public AuthController(AuthenticationFacade authFacade) {
        this.authFacade = authFacade;
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDTO> login(@Valid @RequestBody UserLoginRequestDTO request) {
        return ResponseEntity.ok(authFacade.loginUser(request));
    }

    @PostMapping("/start-forgot-password")
    public ResponseEntity<String> initializingForgotPassword(@RequestParam String email) {
        authFacade.initializeForgotPassword(email);
        return ResponseEntity.ok("An OTP Code is sent to the Email Address.");
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<UserResponseDTO> forgotPassword(@Validated @RequestBody ForgotPasswordRequestDTO req) {
        return ResponseEntity.ok(authFacade.forgotPassword(req));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestParam String jwtToken) {
        authFacade.logout(jwtToken);
        return ResponseEntity.ok("The User successfully logged out.");
    }

//    @PostMapping
//    public ResponseEntity<UserLoginResponseDTO> enterUsername(@Valid @RequestBody UserLoginOtpRequestDTO request) {
//        return ResponseEntity.ok(authFacade.loginUserWithOtpCode());
//    }
//
//    @PostMapping
//    public ResponseEntity<UserLoginResponseDTO> checkOtpCode(@Valid @RequestBody OtpCodeRequestDTO request) {
//
//    }

}
