package io.incondensable.application.mapper;

import io.incondensable.application.business.domain.entity.User;
import io.incondensable.application.web.dto.req.user.UserCreateRequestDTO;
import io.incondensable.application.web.dto.req.user.UserUpdateRequestDTO;
import io.incondensable.application.web.dto.res.UserResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

/**
 * @author abbas
 */
@Mapper
public interface UserMapper {

    UserResponseDTO mapEntityToDto(User user);

    User mapDtoToEntity(UserCreateRequestDTO req);

    void setMappedDtoToUser(@MappingTarget User user, UserUpdateRequestDTO req);

}
