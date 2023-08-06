package io.incondensable.application.web.dto.req.credentials;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author abbas
 */
@Getter
@Setter
@NoArgsConstructor
public class ForgotPasswordRequestDTO {
    private int otpCode;
    private String email;
    private String newPassword;
    private String newPasswordConfirm;
}
