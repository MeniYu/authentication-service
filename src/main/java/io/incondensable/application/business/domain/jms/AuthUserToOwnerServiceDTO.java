package io.incondensable.application.business.domain.jms;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;

/**
 * @author abbas
 */
@Getter
@Setter
@AllArgsConstructor
public class AuthUserToOwnerServiceDTO implements Serializable {

    public static final long serialVersionUID = 704682025354276766L;

    private String userId;
    private String email;
    private String jwt;
    private Instant jwtActiveTime;

}
