package tinysensormanager.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tinysensormanager.model.User;

import java.util.List;

/**
 * This interface is used to access the database {@link User} table.
 * It is used by Spring Data JPA to generate the implementation.
 * @author manokel01
 * @version 1.0.0
 */
@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    /**
     * This method is used to find a {@link User} by username.
     * @param username The username of the {@link User} to find.
     * @return A {@link List} of {@link User} objects.
     */
    List<User> findByLastnameStartingWith(String lastname);

    /**
     * This method is used to find a {@link User} by id.
     * @param id The id of the {@link User} to find.
     * @return A {@link User} object.
     */
    User findUserById(Long id);
}
