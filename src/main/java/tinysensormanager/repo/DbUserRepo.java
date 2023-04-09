package tinysensormanager.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tinysensormanager.model.DbUser;

import java.util.List;

/**
 * This interface is used to access the database {@link DbUser} table.
 * It is used by Spring Data JPA to generate the implementation.
 * @author manokel01
 * @version 1.0.0
 */
@Repository
public interface DbUserRepo extends JpaRepository<DbUser, Long> {
    /**
     * This method is used to find a {@link DbUser} by username.
     * @param username The username of the {@link DbUser} to find.
     * @return A {@link List} of {@link DbUser} objects.
     */
    List<DbUser> findByUsernameEquals(String username);

    /**
     * This method is used to find a {@link DbUser} by username and password.
     * @param username The username of the {@link DbUser} to find.
     * @param password The password of the {@link DbUser} to find.
     * @return A {@link List} of {@link DbUser} objects.
     */
    @Query("SELECT count(*) > 0 FROM DbUser U WHERE U.username = ?1 AND U.password = ?2")
    boolean isUserValid(String username, String password);

    /**
     * This method is used to find a {@link DbUser} by username and password.
     * @param username The username of the {@link DbUser} to find.
     * @param password The password of the {@link DbUser} to find.
     * @return A {@link List} of {@link DbUser} objects.
     */
    @Query("SELECT count(*) > 0 FROM DbUser U WHERE U.username = ?1")
    boolean usernameExists(String username, String password);

    // similar to first method above.
    // @Query("SELECT count(*) > 0 FROM DbUser U WHERE U.username = ?1")
    // boolean getUserByUsername(String email);
}
