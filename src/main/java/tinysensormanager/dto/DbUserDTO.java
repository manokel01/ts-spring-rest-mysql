package tinysensormanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tinysensormanager.model.DbUser;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Data Transfer Object for {@link DbUser} instances.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DbUserDTO {
    /**
     * The id of the {@link DbUser}.
     */
    private Long id;
    /**
     * The username of the {@link DbUser}.
     */
    private String username;

    /**
     * The password of the {@link DbUser}.
     *  {@link DbUser} password must have at least 8 characters,
     *  at least one uppercase letter, at least one lowercase letter and at least one number.
    */
    @Size(min = 8, message = "Password must have at least ${min} characters")
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?\\d).*$")
    private String password;

}
