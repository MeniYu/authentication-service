package io.incondensable.application.business.exceptions.auth;

import io.incondensable.exception.BusinessException;
import org.springframework.http.HttpStatus;

import java.util.Locale;

/**
 * @author abbas
 */
public class GivenOtpExpiredException extends BusinessException {

    public GivenOtpExpiredException(int otpCode) {
        super(new BusinessException(
                "otp.expired",
                new Object[]{otpCode},
                Locale.getDefault(),
                HttpStatus.BAD_REQUEST.value()
        ));
    }

}
