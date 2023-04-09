package tinysensormanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tinysensormanager.dto.UserDTO;
import tinysensormanager.model.User;
import tinysensormanager.repo.UserRepo;
import tinysensormanager.service.exceptions.EntityNotFoundException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * This class implements the {@link IUserService} interface and provides
 * the business logic for handling {@link User} in the system.
 * It uses the {@link UserRepo} interface to interact with the database.
 */
@Service
public class UserServiceImpl implements IUserService {

    /**
     * The repository for interacting with {@link User} entities in the database.
     */
    private final UserRepo userRepo;

    /**
     * Constructor to inject the {@link UserRepo} instance via Spring's dependency injection.
     * @param userRepo The repository for interacting with {@link User} entities in the database.
     */
    @Autowired
    public UserServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    /**
     * Adds a new User entity to the database by saving the {@link User}
     * object converted from the provided {@link UserDTO} object.
     * @param userDTO The {@link UserDTO} object representing the new user to be added.
     * @return The User object that has been added to the database.
     */
    @Transactional
    @Override
    public User addUser(UserDTO userDTO) {
        return userRepo.save(convertToUser(userDTO));
    }

    /**
     * Retrieves a list of all {@link User} entities in the database.
     * @return A list of all User entities in the database.
     */
    @Override
    public List<User> findAllUsers() {
        return userRepo.findAll();
    }

    /**
     * Retrieves a {@link User} entity from the database by its ID.
     * @param id The ID of the {@link User} entity to retrieve.
     * @return The {@link User} entity corresponding to the provided ID.
     * @throws EntityNotFoundException if the {@link User} entity with the provided ID is not found in the database.
     */
    @Override
    public User findUserById(Long id) throws EntityNotFoundException {
        Optional<User> user;
        user = userRepo.findById(id);
        if (user.isEmpty()) throw new EntityNotFoundException(User.class, 0L);
        return user.get();
    }

    /**
     * Updates an existing {@link User} entity in the database by saving the {@link User}
     * object converted from the provided {@link UserDTO} object.
     * @param userDTO The {@link UserDTO} object representing the user to be updated.
     * @return The {@link User} object that has been updated in the database.
     * @throws EntityNotFoundException if the {@link User} entity with the provided ID is not found in the database.
     */
    @Transactional
    @Override
    public User updateUser(UserDTO userDTO) throws EntityNotFoundException {
        // Optional<User> user = userRepo.findById(userDTO.getId());
        // if (user.isEmpty() throw new EntityNotFoundException(User.class, userDTO.getId());
        User user = userRepo.findUserById(userDTO.getId());
        if (user == null) throw new EntityNotFoundException(User.class, userDTO.getId());
        return userRepo.save(convertToUser(userDTO));
    }

    /**
     * Deletes an existing {@link User} entity from the database by its ID.
     * @param id The ID of the {@link User} entity to delete.
     * @throws EntityNotFoundException if the {@link User} entity with the provided ID is not found in the database.
     */
    @Transactional
    @Override
    public void deleteUser(Long id) throws EntityNotFoundException {
        try {
            userRepo.deleteById(id);
        } catch (Exception e) {
            throw new EntityNotFoundException(User.class, id);
        }
    }

    /**
     * Retrieves a list of {@link User} entities from the database by their last name.
     * @param lastname The last name of the {@link User} entities to retrieve.
     * @return A list of {@link User} entities corresponding to the provided last name.
     * @throws EntityNotFoundException if no {@link User} entities with the provided last name are found in the database.
     */
    @Override
    public List<User> findUserByLastname(String lastname) throws EntityNotFoundException {
        List<User> users;
        users = userRepo.findByLastnameStartingWith(lastname);
        if (users.size() == 0) throw new EntityNotFoundException(User.class, 0L);
        return users;
    }

    /**
     * Converts a {@link UserDTO} object to a {@link User} object.
     * @param dto The {@link UserDTO} object to convert.
     * @return The {@link User} object converted from the provided {@link UserDTO} object.
     */
    private static User convertToUser(UserDTO dto) {
        return new User(dto.getId(),
                        dto.getFirstname(),
                        dto.getLastname(),
                        dto.getEmail(),
                        dto.getAddress(),
                        dto.getImageUrl()
        );
    }
}
