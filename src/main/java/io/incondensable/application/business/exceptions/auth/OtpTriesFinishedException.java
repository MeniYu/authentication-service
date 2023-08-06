package io.incondensable.application.business.exceptions.auth;

import io.incondensable.exception.BusinessException;
import org.springframework.http.HttpStatus;

import java.util.Locale;

/**
 * @author abbas
 */
public class OtpTriesFinishedException extends BusinessException {

    public OtpTriesFinishedException() {
        super(new BusinessException(
                "otp.tries.finished",
                null,
                Locale.getDefault(),
                HttpStatus.BAD_REQUEST.value()
        ));
    }

}
