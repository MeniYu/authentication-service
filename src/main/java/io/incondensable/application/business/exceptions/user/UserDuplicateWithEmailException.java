package io.incondensable.application.business.exceptions.user;

import io.incondensable.exception.BusinessException;
import org.springframework.http.HttpStatus;

import java.util.Locale;

/**
 * @author abbas
 */
public class UserDuplicateWithEmailException extends BusinessException {

    public UserDuplicateWithEmailException(String username) {
        super(new BusinessException(
                "user.duplicate.with.email",
                new Object[]{username},
                Locale.getDefault(),
                HttpStatus.NOT_ACCEPTABLE.value()
        ));
    }

}
