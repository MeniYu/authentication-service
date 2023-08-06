package io.incondensable.application.business.exceptions.auth;

import io.incondensable.exception.BusinessException;
import org.springframework.http.HttpStatus;

import java.util.Locale;

/**
 * @author abbas
 */
public class ForgotPasswordsMismatchException extends BusinessException {

    public ForgotPasswordsMismatchException() {
        super(new BusinessException(
                "new.password.does.not.match.with.confirm.password",
                null,
                Locale.getDefault(),
                HttpStatus.BAD_REQUEST.value()
        ));
    }

}
