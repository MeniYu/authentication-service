package io.incondensable.application.business.service.jwt;

import io.incondensable.application.business.domain.entity.Token;
import io.incondensable.application.business.domain.entity.User;
import io.incondensable.application.business.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * @author abbas
 */
@Service
public class TokenService {

    private final UserRepository userRepository;

    public TokenService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void setUserTokenToNull(User user) {
        user.setToken(null);
        userRepository.save(user);
    }

    public void increaseTokenActivationTime(User user) {
        user.setToken(new Token(user.getToken().getJwtString(), user.getToken().getActiveTime().plus(60, ChronoUnit.HOURS)));
        userRepository.save(user);
    }

    public boolean validateToken(String token) {
        User user = userRepository.findUserByToken(token).get();
        Token jwt = user.getToken();
        if (jwt.getActiveTime().isAfter(Instant.now())) {
            setUserTokenToNull(user);
            return false;
        }
        increaseTokenActivationTime(user);
        return true;
    }

}
