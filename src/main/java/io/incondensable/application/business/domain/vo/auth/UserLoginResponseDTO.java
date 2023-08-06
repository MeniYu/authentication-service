package io.incondensable.application.business.domain.vo.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author abbas
 */
@Getter
@Setter
@AllArgsConstructor
public class UserLoginResponseDTO {
    private String userId;
    private String jwt;
    private String username;
    private String role;
}
