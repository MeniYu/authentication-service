package io.incondensable.application.business.exceptions.auth;

import io.incondensable.exception.BusinessException;
import org.springframework.http.HttpStatus;

import java.util.Locale;

/**
 * @author abbas
 */
public class UserNotFoundWithUsernameException extends BusinessException {

    public UserNotFoundWithUsernameException(String username) {
        super(new BusinessException(
                "user.not.found.with.given.username",
                new Object[]{username},
                Locale.getDefault(),
                HttpStatus.NOT_FOUND.value()
        ));
    }

}
