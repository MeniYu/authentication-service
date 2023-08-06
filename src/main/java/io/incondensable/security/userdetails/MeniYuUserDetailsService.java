package io.incondensable.security.userdetails;

import io.incondensable.application.business.domain.entity.User;
import io.incondensable.application.business.exceptions.auth.UserNotFoundWithUsernameException;
import io.incondensable.application.business.repository.UserRepository;
import io.incondensable.exception.BusinessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author abbas
 */
@Service
public class MeniYuUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public MeniYuUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> {
                    throw new UserNotFoundWithUsernameException(username);
                }
        );

        return new MeniYuUserDetails(user);
    }
}
