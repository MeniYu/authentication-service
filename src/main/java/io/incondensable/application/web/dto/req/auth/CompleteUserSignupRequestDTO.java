package io.incondensable.application.web.dto.req.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author abbas
 */
@Getter
@Setter
@NoArgsConstructor
public class CompleteUserSignupRequestDTO {
    private String email;
    private int optCode;
}
