package tinysensormanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import tinysensormanager.model.User;

/**
 * This is a data transfer object (DTO) that represents a {@link User}.
 *
 * @author Your Name
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    /**
     * The ID of the {@link User}.
     */
    private Long id;

    /**
     * The first name of the {@link User}.
     */
    private String firstname;

    /**
     * The last name of the {@link User}.
     */
    private String lastname;

    /**
     * The email address of the {@link User}.
     */
    private String email;

    /**
     * The address of the {@link User}.
     */
    private String address;

    /**
     * The URL of the {@link User}'s profile image.
     */
    private String imageUrl;
}

