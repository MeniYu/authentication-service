package io.incondensable.application.business.domain.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author abbas
 */
@Document(collection = "user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {

    @Id
    private String id;
    private String password;

    @Indexed(unique = true)
    private String username;

    @Indexed(unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;
    private Token token;
    private Otp otp;

    private boolean isEnabled;
}
