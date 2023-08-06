package io.incondensable.application.mapper;

import io.incondensable.application.business.domain.vo.auth.UserLoginInfo;
import io.incondensable.application.web.dto.req.login.UserLoginRequestDTO;
import org.mapstruct.Mapper;

/**
 * @author abbas
 */
@Mapper
public interface UserLoginMapper {

    UserLoginInfo userLoginRequestToUserLoginInfo(UserLoginRequestDTO request);

}
