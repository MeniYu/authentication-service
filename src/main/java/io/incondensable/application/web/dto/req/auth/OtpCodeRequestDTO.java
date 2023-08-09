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
public class OtpCodeRequestDTO {

    @Size(min = 100000, max = 999999)
    private Integer otpCode;

}
