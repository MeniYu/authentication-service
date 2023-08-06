package io.incondensable.application.business.exceptions.auth;

import io.incondensable.exception.BusinessException;
import org.springframework.http.HttpStatus;

import java.util.Locale;

/**
 * @author abbas
 */
public class GivenTokenMismatchException extends BusinessException {

    public GivenTokenMismatchException(String token, String userId) {
        super(new BusinessException(
                "given.jwt.token.does.not.given.user.token",
                new Object[]{token, userId},
                Locale.getDefault(),
                HttpStatus.UNAUTHORIZED.value()
        ));
    }

}
