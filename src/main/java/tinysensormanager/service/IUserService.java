package tinysensormanager.service;

import tinysensormanager.dto.UserDTO;
import tinysensormanager.model.User;
import tinysensormanager.service.exceptions.EntityNotFoundException;

import java.util.List;

/**
  This interface defines the methods that a user service should implement.
        *
        * <p>The methods in this interface allow for the manipulation of {@link User} data.</p>
        *
        * @author manokel01
        * @version 1.0
        */
public interface IUserService {

    /**
     * Adds a new user to the system.
     *
     * @param userDTO the {@link UserDTO} containing the user's information
     * @return the newly created {@link User}
     */
    User addUser(UserDTO userDTO);

    /**
     * Retrieves a list of all users in the system.
     *
     * @return a list of all {@link User}s
     */
    List<User> findAllUsers();

    /**
     * Retrieves a user by their ID.
     *
     * @param id the ID of the user to retrieve
     * @return the {@link User} with the specified ID
     * @throws EntityNotFoundException if the {@link User} with the specified ID is not found
     */
    User findUserById(Long id) throws EntityNotFoundException;

    /**
     * Updates an existing user in the system.
     *
     * @param userDTO the {@link UserDTO} containing the updated user information
     * @return the updated {@link User}
     * @throws EntityNotFoundException if the {@link User} with the specified ID is not found
     */
    User updateUser(UserDTO userDTO) throws EntityNotFoundException;

    /**
     * Deletes a user from the system.
     *
     * @param id the ID of the user to delete
     * @throws EntityNotFoundException if the {@link User} with the specified ID is not found
     */
    void deleteUser(Long id) throws EntityNotFoundException;

    /**
     * Retrieves a list of users with the specified last name.
     *
     * @param lastname the last name to search for
     * @return a list of {@link User}s with the specified last name
     * @throws EntityNotFoundException if no {@link User}s with the specified last name are found
     */
    List<User> findUserByLastname(String lastname) throws EntityNotFoundException;
}
