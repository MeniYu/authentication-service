package io.incondensable.application.business;

import io.incondensable.application.business.domain.entity.User;
import io.incondensable.application.business.exceptions.auth.GivenTokenMismatchException;
import io.incondensable.application.business.service.jwt.TokenService;
import io.incondensable.application.business.service.user.UserService;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author abbas
 */
@Service
public class TokenFacade {

    private final UserService userService;
    private final TokenService tokenService;

    public TokenFacade(UserService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    public String getToken(String userId) {
        return userService.getById(userId).getToken().getJwtString();
    }

    public boolean isTokenStillValid(String userId, String token) {
        User user = userService.getById(userId);
        if (!Objects.equals(user.getToken().getJwtString(), token))
            throw new GivenTokenMismatchException(token, userId);

        return tokenService.validateToken(token);
    }

}
