package io.incondensable.security.jwt;

import io.incondensable.exception.BusinessException;
import org.springframework.http.HttpStatus;

import java.util.Locale;

/**
 * @author abbas
 */
public class JwtTokenIsExpiredException extends BusinessException {

    public JwtTokenIsExpiredException() {
        super(new BusinessException(
                "jwt.token.is.expired",
                null,
                Locale.getDefault(),
                HttpStatus.UNAUTHORIZED.value())
        );
    }

}
