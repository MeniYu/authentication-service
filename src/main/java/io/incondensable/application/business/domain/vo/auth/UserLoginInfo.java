package io.incondensable.application.business.domain.vo.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author abbas
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginInfo {
    //    private String userStringIdentity; //it must be E-Mail Address or Username
    private String username;
    private String password;
//    private String otpCode;
}
