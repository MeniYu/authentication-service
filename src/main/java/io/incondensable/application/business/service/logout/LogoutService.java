package io.incondensable.application.business.service.logout;

import io.incondensable.application.business.exceptions.auth.UserNotFoundWithEmailException;
import io.incondensable.application.business.repository.UserRepository;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * @author abbas
 */
@Service
public class LogoutService {

    private final UserRepository userRepository;

    public LogoutService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void logout(String jwt) {
        userRepository.findUserByToken(jwt).ifPresentOrElse(
                u -> {
                    SecurityContext context = SecurityContextHolder.getContext();
                    context.setAuthentication(null);
                    SecurityContextHolder.clearContext();

                    u.setToken(null);
                    userRepository.save(u);
                }, () -> {
                    throw new UserNotFoundWithEmailException(jwt);
                }
        );
    }

}
