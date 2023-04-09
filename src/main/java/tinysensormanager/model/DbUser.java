package tinysensormanager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Java bean for Database Users {@link DbUser} stored in the database.
 * Implements Serializable to be able to be sent over the network.
 *
 * @author manokel01
 * @version 1.0.0
*/

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "DBUSERS")
public class DbUser implements Serializable {
    /**
     * The id of the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private Long id;
    /**
     * The username of the user.
     */
    @Column(name = "USERNAME", nullable = false, unique = true)
    private String username;

    /**
     * The password of the user.
     */
    @Column(name = "PASSWORD", nullable = false)
    private String password;

}
