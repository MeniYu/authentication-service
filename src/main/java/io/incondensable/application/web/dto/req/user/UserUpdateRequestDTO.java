package io.incondensable.application.web.dto.req.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class UserUpdateRequestDTO {
    private String firstname;
    private String lastname;

    @NotNull
    @NotBlank
    @Size(min = 8)
    private String password;
    private String username;

    @Email
    @NotNull
    @NotBlank
    private String email;
}
