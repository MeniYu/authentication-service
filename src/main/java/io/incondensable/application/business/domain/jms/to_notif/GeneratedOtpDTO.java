package io.incondensable.application.business.domain.jms.to_notif;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author abbas
 */
@Getter
@Setter
@AllArgsConstructor
public class GeneratedOtpDTO {
    private String subject;
    private String userId;
    private String emailAddress;
    private int otpCode;
}
