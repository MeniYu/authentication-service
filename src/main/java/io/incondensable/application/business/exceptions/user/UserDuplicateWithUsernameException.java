package io.incondensable.application.business.exceptions.user;

import io.incondensable.exception.BusinessException;
import org.springframework.http.HttpStatus;

import java.util.Locale;

/**
 * @author abbas
 */
public class UserDuplicateWithUsernameException extends BusinessException {

    public UserDuplicateWithUsernameException(String username) {
        super(new BusinessException(
                "user.duplicate.with.username",
                new Object[]{username},
                Locale.getDefault(),
                HttpStatus.NOT_ACCEPTABLE.value()
        ));
    }

}
