package io.incondensable.application.business.exceptions.auth;

import io.incondensable.exception.BusinessException;
import org.springframework.http.HttpStatus;

import java.util.Locale;

/**
 * @author abbas
 */
public class UserNotFoundWithEmailException extends BusinessException {

    public UserNotFoundWithEmailException(String email) {
        super(new BusinessException(
                "user.not.found.with.given.email",
                new Object[]{email},
                Locale.getDefault(),
                HttpStatus.NOT_FOUND.value()
        ));
    }

}
