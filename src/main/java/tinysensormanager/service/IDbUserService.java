package tinysensormanager.service;

import tinysensormanager.dto.DbUserDTO;
import tinysensormanager.model.DbUser;
import tinysensormanager.service.exceptions.EntityNotFoundException;

import java.util.List;

/**
 * This interface defines the methods that a database user service should implement.
 *
 * <p>The methods in this interface allow for the manipulation of {@link DbUser} data.</p>
 *
 * @author manokel01
 * @version 1.0
 */
public interface IDbUserService {

    /**
     * Registers a new user in the system.
     *
     * @param userDTO the {@link DbUserDTO} containing the user's information
     * @return the newly registered {@link DbUser}
     */
    DbUser registerUser(DbUserDTO userDTO);

    /**
     * Retrieves a list of all users in the system.
     *
     * @return a list of all {@link DbUser}s
     */
    List<DbUser> findAllUsers();

    /**
     * Retrieves a user by their ID.
     *
     * @param id the ID of the user to retrieve
     * @return the {@link DbUser} with the specified ID
     * @throws EntityNotFoundException if the {@link DbUser} with the specified ID is not found
     */
    DbUser findUserById(Long id) throws EntityNotFoundException;

    /**
     * Updates an existing user in the system.
     *
     * @param dbUserDTO the {@link DbUserDTO} containing the updated user information
     * @return the updated {@link DbUser}
     * @throws EntityNotFoundException if the {@link DbUser} with the specified ID is not found
     */
    DbUser updateUser(DbUserDTO dbUserDTO) throws EntityNotFoundException;

    /**
     * Deletes a user from the system.
     *
     * @param id the ID of the user to delete
     * @throws EntityNotFoundException if the {@link DbUser} with the specified ID is not found
     */
    void deleteUser(Long id) throws EntityNotFoundException;

    /**
     * Retrieves a list of users with the specified username.
     *
     * @param username the username to search for
     * @return a list of {@link DbUser}s with the specified username
     * @throws EntityNotFoundException if no {@link DbUser}s with the specified username are found
     */
    List<DbUser> findUserByUsername(String username) throws EntityNotFoundException;

    /**
     * Checks if a username exists in the system.
     *
     * @param username the username to check for existence
     * @return true if the username exists, false otherwise
     */
    boolean usernameExists(String username);
}

