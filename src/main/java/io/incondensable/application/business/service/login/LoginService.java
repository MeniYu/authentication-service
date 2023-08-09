package io.incondensable.application.business.service.login;

import io.incondensable.application.business.domain.entity.User;
import io.incondensable.application.web.dto.req.auth.UserLoginRequestDTO;

import java.util.Optional;

/**
 * @author abbas
 */
public sealed interface LoginService permits ILoginService {

    Optional<User> validateUser(UserLoginRequestDTO dto);

    User login(User user);

}
