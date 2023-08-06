package io.incondensable.application.business.exceptions.user;

import io.incondensable.exception.BusinessException;
import org.springframework.http.HttpStatus;

import java.util.Locale;

/**
 * @author abbas
 */
public class UserNotFoundWithIdException extends BusinessException {

    public UserNotFoundWithIdException(String userId) {
        super(new BusinessException(
                "user.not.found.with.given.id",
                new Object[]{userId},
                Locale.getDefault(),
                HttpStatus.NOT_FOUND.value()
        ));
    }

}
