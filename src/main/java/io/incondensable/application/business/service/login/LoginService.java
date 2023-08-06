package io.incondensable.application.business.service.login;

import io.incondensable.application.business.domain.entity.User;
import io.incondensable.application.business.domain.vo.auth.UserLoginInfo;

import java.util.Optional;

/**
 * @author abbas
 */
public sealed interface LoginService permits ILoginService {

    Optional<User> validateUser(UserLoginInfo loginInfo);

    User login(User user);

}
