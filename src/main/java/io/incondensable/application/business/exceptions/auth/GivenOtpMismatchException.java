package io.incondensable.application.business.exceptions.auth;

import io.incondensable.exception.BusinessException;
import org.springframework.http.HttpStatus;

import java.util.Locale;

/**
 * @author abbas
 */
public class GivenOtpMismatchException extends BusinessException {

    public GivenOtpMismatchException(int givenOtpCode, int triesRemaining) {
        super(new BusinessException(
                "otp.code.mismatch",
                new Object[]{givenOtpCode, triesRemaining},
                Locale.getDefault(),
                HttpStatus.BAD_REQUEST.value()
        ));
    }

}
