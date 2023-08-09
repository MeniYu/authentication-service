package io.incondensable.application.web.dto.req.auth;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author abbas
 */
@Getter
@Setter
@NoArgsConstructor
public class UserLoginRequestDTO {
//    private String userStringIdentity; //it must be E-Mail Address or Username

    private String username;

    @Size(min = 8, max = 24)
    private String password;

}
