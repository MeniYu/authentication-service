package io.incondensable.application.web.dto.req.login;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author abbas
 */
@Getter
@Setter
@NoArgsConstructor
public class UserLoginOtpRequestDTO {
    private String userStringIdentity; //it must be E-Mail Address or Username
}
