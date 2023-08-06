package io.incondensable.application.business.exceptions.auth;

import io.incondensable.exception.BusinessException;
import org.springframework.http.HttpStatus;

import java.util.Locale;

/**
 * @author abbas
 */
public class PasswordMismatchException extends BusinessException {

    public PasswordMismatchException() {
        super(new BusinessException(
                "password.mismatch",
                null,
                Locale.getDefault(),
                HttpStatus.FORBIDDEN.value()
        ));
    }

}
