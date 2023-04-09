package tinysensormanager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Java bean for {@link User} instances stored in the database.
 * Implements Serializable to be able to be sent over the network.
 *
 * @author manokel01
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "USERS")
public class User implements Serializable {
    /**
     * The id of the {@link User}.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private Long id;

    /**
     * The firstname of the {@link User}.
     */
    @Column(name = "FIRSTNAME")
    private String firstname;
    /**
     * The lastname of the {@link User}.
     */
    @Column(name = "LASTNAME")
    private String lastname;
    /**
     * The email of the {@link User}.
     */
    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;
    /**
     * The address of the {@link User}.
     */
    @Column(name = "ADDRESS")
    private String address;
    /**
     * The image url of the {@link User}.
     */
    @Column(name = "IMAGE")
    private String imageUrl;
}


